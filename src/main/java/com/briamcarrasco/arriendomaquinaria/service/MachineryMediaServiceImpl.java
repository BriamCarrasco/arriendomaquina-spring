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

    @Override
    public List<MachineryMedia> getMediaByMachinery(Long machineryId) {
        return mediaRepository.findByMachineryId(machineryId);
    }

    @Override
    public MachineryMedia addImage(Long machineryId, String imageUrl) {
        Machinery machinery = machineryRepository.findById(machineryId)
                .orElseThrow(() -> new IllegalArgumentException(MACHINERY_NOT_FOUND));
        MachineryMedia media = new MachineryMedia();
        media.setMachinery(machinery);
        media.setImgUrl(imageUrl);
        return mediaRepository.save(media);
    }

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
                extension = ".img"; // genérico si no reconocido, aún imagen
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

    @Override
    public MachineryMedia addVideo(Long machineryId, String videoUrl) {
        Machinery machinery = machineryRepository.findById(machineryId)
                .orElseThrow(() -> new IllegalArgumentException(MACHINERY_NOT_FOUND));
        MachineryMedia media = new MachineryMedia();
        media.setMachinery(machinery);
        media.setVidUrl(videoUrl);
        return mediaRepository.save(media);
    }

    @Override
    public void deleteMedia(Long mediaId) {
        mediaRepository.deleteById(mediaId);
    }
}
