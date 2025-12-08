package com.briamcarrasco.arriendomaquinaria.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.model.MachineryMedia;
import com.briamcarrasco.arriendomaquinaria.repository.MachineryMediaRepository;
import com.briamcarrasco.arriendomaquinaria.repository.MachineryRepository;

@ExtendWith(MockitoExtension.class)
class MachineryMediaServiceImplTest {

    @Mock
    private MachineryMediaRepository mediaRepository;

    @Mock
    private MachineryRepository machineryRepository;

    @InjectMocks
    private MachineryMediaServiceImpl service;

    @Test
    void getMediaByMachinery_returnsListFromRepository() {
        MachineryMedia m1 = mock(MachineryMedia.class);
        MachineryMedia m2 = mock(MachineryMedia.class);

        when(mediaRepository.findByMachineryId(1L)).thenReturn(Arrays.asList(m1, m2));

        List<MachineryMedia> res = service.getMediaByMachinery(1L);

        assertNotNull(res);
        assertEquals(2, res.size());
        verify(mediaRepository, times(1)).findByMachineryId(1L);
    }

    @Test
    void addImage_withExistingMachinery_savesMedia() {
        Machinery machinery = mock(Machinery.class);
        when(machineryRepository.findById(10L)).thenReturn(Optional.of(machinery));
        when(mediaRepository.save(any(MachineryMedia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MachineryMedia saved = service.addImage(10L, "http://img");

        assertNotNull(saved);
        assertEquals("http://img", saved.getImgUrl());
        assertSame(machinery, saved.getMachinery());
        verify(mediaRepository).save(any(MachineryMedia.class));
    }

    @Test
    void addImage_whenMachineryNotFound_throws() {
        when(machineryRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.addImage(99L, "url"));
        assertEquals("Maquinaria no encontrada", ex.getMessage());
    }

    @Test
    void addVideo_withExistingMachinery_savesMedia() {
        Machinery machinery = mock(Machinery.class);
        when(machineryRepository.findById(20L)).thenReturn(Optional.of(machinery));
        when(mediaRepository.save(any(MachineryMedia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MachineryMedia saved = service.addVideo(20L, "http://vid");

        assertNotNull(saved);
        assertEquals("http://vid", saved.getVidUrl());
        assertSame(machinery, saved.getMachinery());
        verify(mediaRepository).save(any(MachineryMedia.class));
    }

    @Test
    void addVideo_whenMachineryNotFound_throws() {
        when(machineryRepository.findById(100L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.addVideo(100L, "v"));
        assertEquals("Maquinaria no encontrada", ex.getMessage());
    }

    @Test
    void addImageFile_withValidImage_savesAndUsesUploadDir() throws Exception {
        Path tmp = Files.createTempDirectory("test-uploads-");
        try {
            Field f = service.getClass().getDeclaredField("uploadDir");
            f.setAccessible(true);
            f.set(service, tmp.toString());

            Machinery machinery = mock(Machinery.class);
            when(machineryRepository.findById(5L)).thenReturn(Optional.of(machinery));
            when(mediaRepository.save(any(MachineryMedia.class))).thenAnswer(inv -> inv.getArgument(0));

            MultipartFile file = mock(MultipartFile.class);
            when(file.isEmpty()).thenReturn(false);
            when(file.getContentType()).thenReturn("image/jpeg");
            doAnswer(inv -> {
                return null;
            }).when(file).transferTo(any(java.nio.file.Path.class));

            MachineryMedia saved = service.addImageFile(5L, file);

            assertNotNull(saved);
            assertNotNull(saved.getImgUrl());
            assertTrue(saved.getImgUrl().startsWith("/uploads/"));
            verify(mediaRepository).save(any(MachineryMedia.class));
        } finally {
            // cleanup
            Files.walk(tmp)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException ignored) {
                            // ignore
                        }
                    });
        }
    }

    @Test
    void addImageFile_withNullOrEmptyFile_throws() {
        assertThrows(IllegalArgumentException.class, () -> service.addImageFile(1L, null));

        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.addImageFile(1L, emptyFile));
    }

    @Test
    void addImageFile_withInvalidContentType_throws() {
        MultipartFile bad = mock(MultipartFile.class);
        when(bad.isEmpty()).thenReturn(false);
        when(bad.getContentType()).thenReturn("application/pdf");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.addImageFile(1L, bad));
        assertEquals("Tipo de archivo no permitido", ex.getMessage());
    }

    @Test
    void addImageFile_withNullContentType_throws() {
        MultipartFile badNull = mock(MultipartFile.class);
        when(badNull.isEmpty()).thenReturn(false);
        when(badNull.getContentType()).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.addImageFile(1L, badNull));
        assertEquals("Tipo de archivo no permitido", ex.getMessage());
    }

    @Test
    void addImageFile_whenTransferFails_throwsIllegalState() throws Exception {
        Path tmp = Files.createTempDirectory("test-uploads-");
        try {
            Field f = service.getClass().getDeclaredField("uploadDir");
            f.setAccessible(true);
            f.set(service, tmp.toString());

            MultipartFile file = mock(MultipartFile.class);
            when(file.isEmpty()).thenReturn(false);
            when(file.getContentType()).thenReturn("image/png");
            // el servicio llama transferTo(Path), así que lanzamos la excepción en esa
            // firma
            doThrow(new IOException("io err")).when(file).transferTo(any(java.nio.file.Path.class));

            IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.addImageFile(2L, file));
            assertTrue(ex.getMessage().contains("Error guardando archivo"));
        } finally {
            Files.walk(tmp)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException ignored) {
                            // ignore
                        }
                    });
        }
    }

    @Test
    void addImageFile_withWebpContentType_savesWithWebpExtension() throws Exception {
        Path tmp = Files.createTempDirectory("test-uploads-");
        try {
            Field f = service.getClass().getDeclaredField("uploadDir");
            f.setAccessible(true);
            f.set(service, tmp.toString());

            Machinery machinery = mock(Machinery.class);
            when(machineryRepository.findById(7L)).thenReturn(Optional.of(machinery));
            when(mediaRepository.save(any(MachineryMedia.class))).thenAnswer(inv -> inv.getArgument(0));

            MultipartFile file = mock(MultipartFile.class);
            when(file.isEmpty()).thenReturn(false);
            when(file.getContentType()).thenReturn("image/webp");
            doAnswer(inv -> {
                // simulate successful transfer creating the file on disk
                java.nio.file.Path target = inv.getArgument(0);
                Files.createFile(target);
                return null;
            }).when(file).transferTo(any(java.nio.file.Path.class));

            MachineryMedia saved = service.addImageFile(7L, file);

            assertNotNull(saved);
            assertTrue(saved.getImgUrl().startsWith("/uploads/"));
            assertTrue(saved.getImgUrl().endsWith(".webp"));
            verify(mediaRepository).save(any(MachineryMedia.class));
        } finally {
            Files.walk(tmp)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException ignored) {
                            // ignore
                        }
                    });
        }
    }

    @Test
    void addImageFile_fileSavedButMachineryNotFound_fileRemainsAndThrows() throws Exception {
        Path tmp = Files.createTempDirectory("test-uploads-");
        try {
            Field f = service.getClass().getDeclaredField("uploadDir");
            f.setAccessible(true);
            f.set(service, tmp.toString());
            when(machineryRepository.findById(42L)).thenReturn(Optional.empty());

            MultipartFile file = mock(MultipartFile.class);
            when(file.isEmpty()).thenReturn(false);
            when(file.getContentType()).thenReturn("image/gif");
            doAnswer(inv -> {
                java.nio.file.Path target = inv.getArgument(0);
                Files.createFile(target);
                return null;
            }).when(file).transferTo(any(java.nio.file.Path.class));

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> service.addImageFile(42L, file));
            assertEquals("Maquinaria no encontrada", ex.getMessage());
            long filesCount = Files.list(tmp).count();
            assertTrue(filesCount >= 1);
        } finally {
            Files.walk(tmp)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException ignored) {
                            // ignore
                        }
                    });
        }
    }

    @Test
    void deleteMedia_callsRepositoryDelete() {
        service.deleteMedia(123L);
        verify(mediaRepository).deleteById(123L);
    }
}
