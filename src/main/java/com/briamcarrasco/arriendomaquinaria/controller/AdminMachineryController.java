package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.model.Category;
import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.service.MachineryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;

/**
 * Controlador para la administración de maquinarias en el sistema.
 * Permite crear, buscar, actualizar, eliminar y listar maquinarias mediante la
 * API.
 * También gestiona la búsqueda de maquinarias por nombre o categoría para la
 * vista.
 */
@Controller
@RequestMapping("/api/machinery")
@Validated
@Slf4j
public class AdminMachineryController {

    private static final Logger logger = LoggerFactory.getLogger(AdminMachineryController.class);

    private final MachineryService machineryService;

    public AdminMachineryController(MachineryService machineryService) {
        this.machineryService = machineryService;
    }

    /**
     * Crea una nueva maquinaria en el sistema.
     *
     * @param nameMachinery nombre de la maquinaria
     * @param categoryId    identificador de la categoría
     * @param status        estado de la maquinaria
     * @param pricePerDay   precio por día de arriendo
     * @param imageUrl      URL de la imagen de la maquinaria (opcional)
     * @return redirección a la página principal
     */
    @PostMapping
    public String createMachinery(
            @RequestParam("nameMachinery") String nameMachinery,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("status") String status,
            @RequestParam("pricePerDay") BigDecimal pricePerDay,
            @RequestParam("imageFile") MultipartFile imageFile) {

        logger.info(
                "Recibida solicitud para crear maquinaria: nameMachinery={}, categoryId={}, status={}, pricePerDay={}, imageFile={}",
                nameMachinery, categoryId, status, pricePerDay,
                imageFile != null ? imageFile.getOriginalFilename() : "null");

        Machinery machinery = new Machinery();
        machinery.setNameMachinery(nameMachinery);
        machinery.setStatus(status);
        machinery.setPricePerDay(pricePerDay);

        Category category = new Category();
        category.setId(categoryId);
        machinery.setCategory(category);
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String uploadDir = "src/main/resources/static/images/";
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, imageFile.getBytes());
                machinery.setImageUrl("/images/" + fileName);
                logger.info("Imagen subida y guardada en: {}", filePath);
            } catch (Exception e) {
                logger.error("Error al guardar la imagen", e);
                machinery.setImageUrl("/images/Case_IH_Axial-Flow.png");
            }
        } else {
            machinery.setImageUrl("/images/Case_IH_Axial-Flow.png");
            logger.info("No se proporcionó imagen, se asigna imagen por defecto.");
        }

        try {
            machineryService.createMachinery(machinery);
            logger.info("Maquinaria creada exitosamente: {}", machinery);
        } catch (Exception e) {
            logger.error("Error al crear maquinaria", e);
            return "redirect:/error";
        }
        return "redirect:/home";
    }

    /**
     * Obtiene la lista de todas las maquinarias registradas.
     *
     * @return respuesta HTTP con la lista de maquinarias
     */
    @GetMapping
    public ResponseEntity<List<Machinery>> findAll() {
        return ResponseEntity.ok(machineryService.findAll());
    }

    /**
     * Busca una maquinaria por su identificador.
     *
     * @param id identificador de la maquinaria
     * @return respuesta HTTP con la maquinaria encontrada o not found si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Machinery> findById(@PathVariable Long id) {
        Optional<Machinery> machinery = machineryService.findById(id);
        return machinery.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza los datos de una maquinaria existente.
     *
     * @param id        identificador de la maquinaria a actualizar
     * @param machinery datos actualizados de la maquinaria
     * @return respuesta HTTP con la maquinaria actualizada o not found si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Machinery> updateMachinery(@PathVariable Long id, @RequestBody Machinery machinery) {
        try {
            Machinery updated = machineryService.updateMachinery(id, machinery);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina una maquinaria por su identificador.
     *
     * @param id identificador de la maquinaria a eliminar
     * @return respuesta HTTP sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMachinery(@PathVariable Long id) {
        machineryService.deleteMachinery(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Tipos permitidos de búsqueda.
     */
    public enum TipoBusqueda {
        NOMBRE, CATEGORIA;

        public static TipoBusqueda from(String value) {
            return TipoBusqueda.valueOf(value.trim().toUpperCase());
        }
    }

    /**
     * Busca maquinarias por nombre o categoría y muestra el resultado en la vista
     * de búsqueda.
     * Se aplica sanitización de entradas para prevenir ataques XSS.
     *
     * @param name     nombre de la maquinaria (opcional)
     * @param category nombre de la categoría (opcional)
     * @param tipo     tipo de búsqueda: "nombre" o "categoria"
     * @param model    modelo para la vista
     * @return nombre de la vista de búsqueda
     */
    @GetMapping("/search")
    public String buscarMaquinaria(
            @RequestParam(required = false) @Size(max = 50, message = "El nombre no debe superar 50 caracteres") @Pattern(regexp = "[\\p{L}\\p{N} .-]*", message = "Nombre contiene caracteres no permitidos") String name,
            @RequestParam(required = false) @Size(max = 50, message = "La categoría no debe superar 50 caracteres") @Pattern(regexp = "[\\p{L}\\p{N} .-]*", message = "Categoría contiene caracteres no permitidos") String category,
            @RequestParam String tipo,
            Model model) {
        List<Machinery> maquinarias = List.of();

        String trimmedName = name == null ? null : name.trim();
        String trimmedCategory = category == null ? null : category.trim();

        TipoBusqueda tipoBusqueda = TipoBusqueda.from(tipo);

        if (tipoBusqueda == TipoBusqueda.NOMBRE && trimmedName != null && !trimmedName.isEmpty()) {
            maquinarias = machineryService.findByNameMachinery(trimmedName);
        } else if (tipoBusqueda == TipoBusqueda.CATEGORIA && trimmedCategory != null && !trimmedCategory.isEmpty()) {
            maquinarias = machineryService.findByCategory(trimmedCategory);
        }

        model.addAttribute("maquinarias", maquinarias);
        return "search";
    }
}