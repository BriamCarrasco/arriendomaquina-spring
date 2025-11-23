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

@RestController
@RequestMapping("/api/machinery-media")
public class MachineryMediaController {

    private final MachineryMediaService mediaService;

    public MachineryMediaController(MachineryMediaService mediaService) {
        this.mediaService = mediaService;
    }



    @GetMapping("/machinery/{machineryId}")
    public ResponseEntity<List<MachineryMedia>> list(@PathVariable Long machineryId) {
        return ResponseEntity.ok(mediaService.getMediaByMachinery(machineryId));
    }

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

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping(value = "/machinery/{machineryId}/image-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadImage(@PathVariable Long machineryId,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            MachineryMedia media = mediaService.addImageFile(machineryId, file);
            return ResponseEntity.ok(media);
        } catch (org.springframework.dao.DataIntegrityViolationException dive) {
            // DB may have a leftover unique constraint; return 409 with message for client
            return ResponseEntity.status(409).body(Map.of(Constants.ERROR_KEY, Constants.DUPLICATE_MEDIA_ERROR, 
                    Constants.MESSAGE_KEY,
                    Constants.UNIQUE_CONSTRAINT_ERROR_MSG));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(Constants.ERROR_KEY, "invalid_file", Constants.MESSAGE_KEY, e.getMessage()));
        }
    }

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
            return ResponseEntity.badRequest().body(Map.of(Constants.ERROR_KEY, "invalid", Constants.MESSAGE_KEY, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> delete(@PathVariable Long mediaId) {
        mediaService.deleteMedia(mediaId);
        return ResponseEntity.noContent().build();
    }
}
