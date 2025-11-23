package com.briamcarrasco.arriendomaquinaria.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.model.MachineryMedia;
import com.briamcarrasco.arriendomaquinaria.repository.MachineryMediaRepository;
import com.briamcarrasco.arriendomaquinaria.repository.MachineryRepository;

/**
 * Implementación del servicio para la gestión de archivos multimedia asociados
 * a maquinarias.
 * 
 * Permite agregar imágenes y videos a una maquinaria, obtener archivos
 * multimedia por maquinaria,
 * subir archivos de imagen y eliminar archivos multimedia.
 */
@Service
@Transactional
public class MachineryMediaServiceImpl implements MachineryMediaService {

    private final MachineryMediaRepository mediaRepository;
    private final MachineryRepository machineryRepository;
    @Value("${app.upload-dir:uploads}")
    private String uploadDir;
    private static final String MACHINERY_NOT_FOUND = "Maquinaria no encontrada";

    public MachineryMediaServiceImpl(MachineryMediaRepository mediaRepository,
            MachineryRepository machineryRepository) {
        this.mediaRepository = mediaRepository;
        this.machineryRepository = machineryRepository;
    }

    /**
     * Obtiene la lista de archivos multimedia asociados a una maquinaria.
     *
     * @param machineryId identificador de la maquinaria
     * @return lista de objetos MachineryMedia
     */
    @Override
    public List<MachineryMedia> getMediaByMachinery(Long machineryId) {
        return mediaRepository.findByMachineryId(machineryId);
    }

    /**
     * Agrega una imagen a una maquinaria usando una URL.
     *
     * @param machineryId identificador de la maquinaria
     * @param imageUrl    URL de la imagen
     * @return el objeto MachineryMedia creado
     */
    @Override
    public MachineryMedia addImage(Long machineryId, String imageUrl) {
        Machinery machinery = machineryRepository.findById(machineryId)
                .orElseThrow(() -> new IllegalArgumentException(MACHINERY_NOT_FOUND));
        MachineryMedia media = new MachineryMedia();
        media.setMachinery(machinery);
        media.setImgUrl(imageUrl);
        return mediaRepository.save(media);
    }

    /**
     * Agrega una imagen a una maquinaria subiendo un archivo.
     *
     * @param machineryId identificador de la maquinaria
     * @param file        archivo de imagen a subir
     * @return el objeto MachineryMedia creado
     * @throws IllegalArgumentException si el archivo es vacío o el tipo no es
     *                                  permitido
     * @throws IllegalStateException    si ocurre un error al guardar el archivo
     */
    @Override
    public MachineryMedia addImageFile(Long machineryId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Archivo vacío");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Tipo de archivo no permitido");
        }

        String extension;
        switch (contentType) {
            case "image/jpeg":
                extension = ".jpg";
                break;
            case "image/png":
                extension = ".png";
                break;
            case "image/webp":
                extension = ".webp";
                break;
            default:
                extension = ".img";
        }

        String filename = UUID.randomUUID().toString() + extension;
        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(dir);
            Path target = dir.resolve(filename);
            file.transferTo(target);
        } catch (IOException e) {
            throw new IllegalStateException("Error guardando archivo", e);
        }

        Machinery machinery = machineryRepository.findById(machineryId)
                .orElseThrow(() -> new IllegalArgumentException(MACHINERY_NOT_FOUND));
        MachineryMedia media = new MachineryMedia();
        media.setMachinery(machinery);
        media.setImgUrl("/uploads/" + filename);
        return mediaRepository.save(media);
    }

    /**
     * Agrega un video a una maquinaria usando una URL.
     *
     * @param machineryId identificador de la maquinaria
     * @param videoUrl    URL del video
     * @return el objeto MachineryMedia creado
     */
    @Override
    public MachineryMedia addVideo(Long machineryId, String videoUrl) {
        Machinery machinery = machineryRepository.findById(machineryId)
                .orElseThrow(() -> new IllegalArgumentException(MACHINERY_NOT_FOUND));
        MachineryMedia media = new MachineryMedia();
        media.setMachinery(machinery);
        media.setVidUrl(videoUrl);
        return mediaRepository.save(media);
    }

    /**
     * Elimina un archivo multimedia por su identificador.
     *
     * @param mediaId identificador del archivo multimedia a eliminar
     */
    @Override
    public void deleteMedia(Long mediaId) {
        mediaRepository.deleteById(mediaId);
    }
}