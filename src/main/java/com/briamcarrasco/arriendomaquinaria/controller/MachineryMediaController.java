package com.briamcarrasco.arriendomaquinaria.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.briamcarrasco.arriendomaquinaria.jwt.Constants;

import com.briamcarrasco.arriendomaquinaria.model.MachineryMedia;
import com.briamcarrasco.arriendomaquinaria.service.MachineryMediaService;

/**
 * Controlador REST para la gestión de archivos multimedia asociados a
 * maquinarias.
 * 
 * Permite agregar imágenes y videos a una maquinaria, subir archivos de imagen,
 * obtener archivos multimedia por maquinaria y eliminar archivos multimedia.
 */
@RestController
@RequestMapping("/api/machinery-media")
public class MachineryMediaController {

    private final MachineryMediaService mediaService;

    public MachineryMediaController(MachineryMediaService mediaService) {
        this.mediaService = mediaService;
    }

    /**
     * Obtiene la lista de archivos multimedia asociados a una maquinaria.
     *
     * @param machineryId identificador de la maquinaria
     * @return lista de objetos MachineryMedia
     */
    @GetMapping("/machinery/{machineryId}")
    public ResponseEntity<List<MachineryMedia>> list(@PathVariable Long machineryId) {
        return ResponseEntity.ok(mediaService.getMediaByMachinery(machineryId));
    }

    /**
     * Agrega una imagen a una maquinaria usando una URL.
     * Requiere autenticación con rol USER o ADMIN.
     *
     * @param machineryId identificador de la maquinaria
     * @param body        mapa con la URL de la imagen
     * @return el objeto MachineryMedia creado o error si la URL es inválida o
     *         duplicada
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/machinery/{machineryId}/image")
    public ResponseEntity<Object> addImage(@PathVariable Long machineryId,
            @RequestBody Map<String, String> body) {
        String url = body.get("url");
        if (url == null || url.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(Constants.ERROR_KEY, "empty_url"));
        }
        try {
            MachineryMedia media = mediaService.addImage(machineryId, url);
            return ResponseEntity.ok(media);
        } catch (org.springframework.dao.DataIntegrityViolationException dive) {
            return ResponseEntity.status(409).body(Map.of(Constants.ERROR_KEY, Constants.DUPLICATE_MEDIA_ERROR,
                    Constants.MESSAGE_KEY,
                    Constants.UNIQUE_CONSTRAINT_ERROR_MSG));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(Constants.ERROR_KEY, "invalid",
                    Constants.MESSAGE_KEY, e.getMessage()));
        }
    }

    /**
     * Agrega una imagen a una maquinaria subiendo un archivo.
     * Requiere autenticación con rol USER o ADMIN.
     *
     * @param machineryId identificador de la maquinaria
     * @param file        archivo de imagen a subir
     * @return el objeto MachineryMedia creado o error si el archivo es inválido o
     *         duplicado
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping(value = "/machinery/{machineryId}/image-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadImage(@PathVariable Long machineryId,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            MachineryMedia media = mediaService.addImageFile(machineryId, file);
            return ResponseEntity.ok(media);
        } catch (org.springframework.dao.DataIntegrityViolationException dive) {
            return ResponseEntity.status(409).body(Map.of(Constants.ERROR_KEY, Constants.DUPLICATE_MEDIA_ERROR,
                    Constants.MESSAGE_KEY,
                    Constants.UNIQUE_CONSTRAINT_ERROR_MSG));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(Constants.ERROR_KEY, "invalid_file", Constants.MESSAGE_KEY, e.getMessage()));
        }
    }

    /**
     * Agrega un video a una maquinaria usando una URL.
     * Requiere autenticación con rol USER o ADMIN.
     *
     * @param machineryId identificador de la maquinaria
     * @param body        mapa con la URL del video
     * @return el objeto MachineryMedia creado o error si la URL es inválida o
     *         duplicada
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/machinery/{machineryId}/video")
    public ResponseEntity<Object> addVideo(@PathVariable Long machineryId,
            @RequestBody Map<String, String> body) {
        String url = body.get("url");
        if (url == null || url.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(Constants.ERROR_KEY, "empty_url"));
        }
        try {
            MachineryMedia media = mediaService.addVideo(machineryId, url);
            return ResponseEntity.ok(media);
        } catch (org.springframework.dao.DataIntegrityViolationException dive) {
            return ResponseEntity.status(409).body(Map.of(Constants.ERROR_KEY, Constants.DUPLICATE_MEDIA_ERROR,
                    Constants.MESSAGE_KEY,
                    Constants.UNIQUE_CONSTRAINT_ERROR_MSG));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(Constants.ERROR_KEY, "invalid", Constants.MESSAGE_KEY, e.getMessage()));
        }
    }

    /**
     * Elimina un archivo multimedia por su identificador.
     * Requiere autenticación con rol USER o ADMIN.
     *
     * @param mediaId identificador del archivo multimedia a eliminar
     * @return respuesta sin contenido si la eliminación es exitosa
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> delete(@PathVariable Long mediaId) {
        mediaService.deleteMedia(mediaId);
        return ResponseEntity.noContent().build();
    }
}