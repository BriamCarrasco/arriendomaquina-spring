package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.model.MachineryMedia;
import com.briamcarrasco.arriendomaquinaria.service.MachineryMediaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MachineryMediaControllerTest {

    @Mock
    private MachineryMediaService mediaService;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private MachineryMediaController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------------------------------------------------
    // LIST MEDIA
    // -------------------------------------------------------------

    @Test
    void list_returnsMediaList() {
        List<MachineryMedia> media = List.of(new MachineryMedia());
        when(mediaService.getMediaByMachinery(1L)).thenReturn(media);

        ResponseEntity<List<MachineryMedia>> response = controller.list(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    // -------------------------------------------------------------
    // ADD IMAGE
    // -------------------------------------------------------------

    @Test
    void addImage_success_returnsOk() {
        MachineryMedia media = new MachineryMedia();
        when(mediaService.addImage(1L, "http://img.url")).thenReturn(media);

        ResponseEntity<Object> response = controller.addImage(1L, Map.of("url", "http://img.url"));

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void addImage_emptyUrl_returnsBadRequest() {
        ResponseEntity<Object> response = controller.addImage(1L, Map.of("url", "   "));

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void addImage_nullUrl_returnsBadRequest() {
        ResponseEntity<Object> response = controller.addImage(1L, Map.of());

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void addImage_duplicate_returns409() {
        when(mediaService.addImage(1L, "http://dup.url"))
                .thenThrow(new DataIntegrityViolationException("Duplicate"));

        ResponseEntity<Object> response = controller.addImage(1L, Map.of("url", "http://dup.url"));

        assertEquals(409, response.getStatusCode().value());
    }

    @Test
    void addImage_invalidArgument_returnsBadRequest() {
        when(mediaService.addImage(1L, "http://invalid"))
                .thenThrow(new IllegalArgumentException("Invalid"));

        ResponseEntity<Object> response = controller.addImage(1L, Map.of("url", "http://invalid"));

        assertEquals(400, response.getStatusCode().value());
    }

    // -------------------------------------------------------------
    // UPLOAD IMAGE
    // -------------------------------------------------------------

    @Test
    void uploadImage_success_returnsOk() {
        MachineryMedia media = new MachineryMedia();
        when(mediaService.addImageFile(1L, file)).thenReturn(media);

        ResponseEntity<Object> response = controller.uploadImage(1L, file);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void uploadImage_duplicate_returns409() {
        when(mediaService.addImageFile(1L, file))
                .thenThrow(new DataIntegrityViolationException("Dup"));

        ResponseEntity<Object> response = controller.uploadImage(1L, file);

        assertEquals(409, response.getStatusCode().value());
    }

    @Test
    void uploadImage_illegalArgument_returnsBadRequest() {
        when(mediaService.addImageFile(1L, file))
                .thenThrow(new IllegalArgumentException("Invalid file"));

        ResponseEntity<Object> response = controller.uploadImage(1L, file);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void uploadImage_illegalState_returnsBadRequest() {
        when(mediaService.addImageFile(1L, file))
                .thenThrow(new IllegalStateException("Error saving"));

        ResponseEntity<Object> response = controller.uploadImage(1L, file);

        assertEquals(400, response.getStatusCode().value());
    }

    // -------------------------------------------------------------
    // ADD VIDEO
    // -------------------------------------------------------------

    @Test
    void addVideo_success_returnsOk() {
        MachineryMedia media = new MachineryMedia();
        when(mediaService.addVideo(1L, "http://vid.url")).thenReturn(media);

        ResponseEntity<Object> response = controller.addVideo(1L, Map.of("url", "http://vid.url"));

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void addVideo_emptyUrl_returnsBadRequest() {
        ResponseEntity<Object> response = controller.addVideo(1L, Map.of("url", ""));

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void addVideo_nullUrl_returnsBadRequest() {
        ResponseEntity<Object> response = controller.addVideo(1L, Map.of());

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void addVideo_duplicate_returns409() {
        when(mediaService.addVideo(1L, "http://dup"))
                .thenThrow(new DataIntegrityViolationException("Dup"));

        ResponseEntity<Object> response = controller.addVideo(1L, Map.of("url", "http://dup"));

        assertEquals(409, response.getStatusCode().value());
    }

    @Test
    void addVideo_invalidArgument_returnsBadRequest() {
        when(mediaService.addVideo(1L, "http://bad"))
                .thenThrow(new IllegalArgumentException("Invalid"));

        ResponseEntity<Object> response = controller.addVideo(1L, Map.of("url", "http://bad"));

        assertEquals(400, response.getStatusCode().value());
    }

    // -------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------

    @Test
    void delete_success_returnsNoContent() {
        ResponseEntity<Void> response = controller.delete(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(mediaService).deleteMedia(1L);
    }
}
