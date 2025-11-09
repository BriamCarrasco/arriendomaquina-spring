package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.model.Category;
import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.service.MachineryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.Optional;

/**
 * Controlador para la administración de maquinarias en el sistema.
 * Permite crear, buscar, actualizar, eliminar y listar maquinarias mediante la
 * API.
 * También gestiona la búsqueda de maquinarias por nombre o categoría para la
 * vista.
 */
@Controller
@RequestMapping("/api/machinery")
public class AdminMachineryController {

    @Autowired
    private MachineryService machineryService;

    /**
     * Crea una nueva maquinaria en el sistema.
     *
     * @param nameMachinery nombre de la maquinaria
     * @param categoryId    identificador de la categoría
     * @param status        estado de la maquinaria
     * @param pricePerDay   precio por día de arriendo
     * @return redirección a la página principal
     */
    @PostMapping
    public String createMachinery(
            @RequestParam("nameMachinery") String nameMachinery,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("status") String status,
            @RequestParam("pricePerDay") BigDecimal pricePerDay) {

        Machinery machinery = new Machinery();
        machinery.setNameMachinery(nameMachinery);
        machinery.setStatus(status);
        machinery.setPricePerDay(pricePerDay);

        Category category = new Category();
        category.setId(categoryId);
        machinery.setCategory(category);

        machineryService.createMachinery(machinery);
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
     * Busca maquinarias por nombre o categoría y muestra el resultado en la vista
     * de búsqueda.
     *
     * @param name     nombre de la maquinaria (opcional)
     * @param category nombre de la categoría (opcional)
     * @param tipo     tipo de búsqueda: "nombre" o "categoria"
     * @param model    modelo para la vista
     * @return nombre de la vista de búsqueda
     */
    @GetMapping("/search")
    public String buscarMaquinaria(@RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam String tipo,
            Model model) {
        List<Machinery> maquinarias = List.of();
        if ("nombre".equals(tipo) && name != null && !name.isEmpty()) {
            maquinarias = machineryService.findByNameMachinery(name);
        } else if ("categoria".equals(tipo) && category != null && !category.isEmpty()) {
            maquinarias = machineryService.findByCategory(category);
        }
        model.addAttribute("maquinarias", maquinarias);
        return "search";
    }
}