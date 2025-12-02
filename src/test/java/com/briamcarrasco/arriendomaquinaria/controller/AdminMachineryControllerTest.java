package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.service.MachineryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AdminMachineryControllerTest {

    @Mock
    MachineryService machineryService;

    @Mock
    Model model;

    @InjectMocks
    AdminMachineryController controller;

    @Test
    void createMachinery_withImage_success() {
        MockMultipartFile image = new MockMultipartFile("imageFile", "img.png", "image/png", "data".getBytes());
        when(machineryService.createMachinery(any())).thenReturn(new Machinery());

        String result = controller.createMachinery("Tractor", 1L, "Disponible", BigDecimal.TEN, image);

        assertEquals("redirect:/home", result);
        verify(machineryService).createMachinery(any(Machinery.class));
    }

    @Test
    void createMachinery_withoutImage_success() {
        MockMultipartFile image = new MockMultipartFile("imageFile", "", "image/png", new byte[0]);
        when(machineryService.createMachinery(any())).thenReturn(new Machinery());

        String result = controller.createMachinery("Tractor", 1L, "Disponible", BigDecimal.TEN, image);

        assertEquals("redirect:/home", result);
        verify(machineryService).createMachinery(any(Machinery.class));
    }

    @Test
    void createMachinery_serviceThrows_returnsErrorRedirect() {
        MockMultipartFile image = new MockMultipartFile("imageFile", "img.png", "image/png", "data".getBytes());
        doThrow(new RuntimeException("fail")).when(machineryService).createMachinery(any());

        String result = controller.createMachinery("Tractor", 1L, "Disponible", BigDecimal.TEN, image);

        assertEquals("redirect:/error", result);
        verify(machineryService).createMachinery(any(Machinery.class));
    }

    @Test
    void findAll_returnsList() {
        List<Machinery> list = List.of(new Machinery(), new Machinery());
        when(machineryService.findAll()).thenReturn(list);

        ResponseEntity<List<Machinery>> response = controller.findAll();

        assertEquals(2, response.getBody().size());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void findById_found_returnsOk() {
        Machinery m = new Machinery();
        when(machineryService.findById(1L)).thenReturn(Optional.of(m));

        ResponseEntity<Machinery> response = controller.findById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertSame(m, response.getBody());
    }

    @Test
    void findById_notFound_returns404() {
        when(machineryService.findById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Machinery> response = controller.findById(2L);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void updateMachinery_success_returnsOk() {
        Machinery m = new Machinery();
        when(machineryService.updateMachinery(1L, m)).thenReturn(m);

        ResponseEntity<Machinery> response = controller.updateMachinery(1L, m);

        assertEquals(200, response.getStatusCode().value());
        assertSame(m, response.getBody());
    }

    @Test
    void updateMachinery_notFound_returns404() {
        Machinery m = new Machinery();
        when(machineryService.updateMachinery(1L, m)).thenThrow(new RuntimeException("not found"));

        ResponseEntity<Machinery> response = controller.updateMachinery(1L, m);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void deleteMachinery_callsService_returnsNoContent() {
        ResponseEntity<Void> response = controller.deleteMachinery(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(machineryService).deleteMachinery(1L);
    }

    @Test
    void buscarMaquinaria_byName_addsToModel() {
        List<Machinery> list = List.of(new Machinery());
        when(machineryService.findByNameMachinery("Tractor")).thenReturn(list);

        String view = controller.buscarMaquinaria("Tractor", null, "nombre", model);

        assertEquals("search", view);
        verify(model).addAttribute("maquinarias", list);
    }

    @Test
    void buscarMaquinaria_byCategory_addsToModel() {
        List<Machinery> list = List.of(new Machinery());
        when(machineryService.findByCategory("Agrícola")).thenReturn(list);

        String view = controller.buscarMaquinaria(null, "Agrícola", "categoria", model);

        assertEquals("search", view);
        verify(model).addAttribute("maquinarias", list);
    }

    @Test
    void buscarMaquinaria_emptyParams_addsEmptyList() {
        String view = controller.buscarMaquinaria(null, null, "nombre", model);

        assertEquals("search", view);
        verify(model).addAttribute("maquinarias", List.of());
    }

    @Test
    void tipoBusqueda_fromString_works() {
        assertEquals(AdminMachineryController.TipoBusqueda.NOMBRE,
                AdminMachineryController.TipoBusqueda.from("nombre"));
        assertEquals(AdminMachineryController.TipoBusqueda.CATEGORIA,
                AdminMachineryController.TipoBusqueda.from("categoria"));
    }

    @Test
    void createMachinery_imageSaveThrows_setsDefaultImage() {
        MockMultipartFile image = new MockMultipartFile("imageFile", "img.png", "image/png", "data".getBytes());
        try (MockedStatic<java.nio.file.Files> filesMock = mockStatic(java.nio.file.Files.class)) {
            filesMock.when(() -> java.nio.file.Files.createDirectories(any())).thenThrow(new RuntimeException("fail"));
            when(machineryService.createMachinery(any())).thenReturn(new Machinery());

            String result = controller.createMachinery("Tractor", 1L, "Disponible", BigDecimal.TEN, image);

            assertEquals("redirect:/home", result);
            verify(machineryService)
                    .createMachinery(argThat(m -> "/images/Case_IH_Axial-Flow.png".equals(m.getImageUrl())));
        }
    }
}