package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.service.MachineryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminMachineryControllerTest {

    @Mock
    private MachineryService machineryService;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private Model model;

    @InjectMocks
    private AdminMachineryController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMachinery_WithImage_Success() throws Exception {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("test.png");
        when(multipartFile.getBytes()).thenReturn("fake-image".getBytes());

        String result = controller.createMachinery(
                "Excavadora", 1L, "Disponible",
                new BigDecimal("10000"), multipartFile);

        assertEquals("redirect:/home", result);
        verify(machineryService, times(1)).createMachinery(any(Machinery.class));
    }

    @Test
    void createMachinery_NoImage_DefaultImageAssigned() {
        when(multipartFile.isEmpty()).thenReturn(true);

        String result = controller.createMachinery(
                "Tractor", 2L, "Operativa",
                new BigDecimal("5000"), multipartFile);

        ArgumentCaptor<Machinery> captor = ArgumentCaptor.forClass(Machinery.class);
        verify(machineryService).createMachinery(captor.capture());

        assertEquals("/images/Case_IH_Axial-Flow.png", captor.getValue().getImageUrl());
        assertEquals("redirect:/home", result);
    }

    @Test
    void createMachinery_ErrorSavingImage_UsesDefaultImage() throws Exception {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("bad.png");
        when(multipartFile.getBytes()).thenThrow(new RuntimeException("Error"));

        String result = controller.createMachinery(
                "Camión", 3L, "OK",
                new BigDecimal("7500"), multipartFile);

        assertEquals("redirect:/home", result);
        verify(machineryService).createMachinery(any(Machinery.class));
    }

    @Test
    void createMachinery_ServiceThrows_ReturnsError() {
        when(multipartFile.isEmpty()).thenReturn(true);
        doThrow(new RuntimeException("DB Error"))
                .when(machineryService).createMachinery(any());

        String result = controller.createMachinery(
                "Prensa", 4L, "Disponible",
                new BigDecimal("3000"), multipartFile);

        assertEquals("redirect:/error", result);
    }

    @Test
    void findAll_ReturnsList() {
        List<Machinery> list = List.of(new Machinery());
        when(machineryService.findAll()).thenReturn(list);

        ResponseEntity<List<Machinery>> response = controller.findAll();
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findById_Found() {
        Machinery m = new Machinery();
        when(machineryService.findById(1L)).thenReturn(Optional.of(m));

        ResponseEntity<Machinery> resp = controller.findById(1L);
        assertEquals(200, resp.getStatusCode().value());
        assertEquals(m, resp.getBody());
    }

    @Test
    void findById_NotFound() {
        when(machineryService.findById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Machinery> resp = controller.findById(2L);
        assertEquals(404, resp.getStatusCode().value());
    }

    @Test
    void updateMachinery_Success() {
        Machinery m = new Machinery();
        when(machineryService.updateMachinery(1L, m)).thenReturn(m);

        ResponseEntity<Machinery> resp = controller.updateMachinery(1L, m);

        assertEquals(200, resp.getStatusCode().value());
    }

    @Test
    void updateMachinery_NotFound() {
        Machinery m = new Machinery();
        when(machineryService.updateMachinery(1L, m)).thenThrow(new RuntimeException());

        ResponseEntity<Machinery> resp = controller.updateMachinery(1L, m);

        assertEquals(404, resp.getStatusCode().value());
    }

    @Test
    void deleteMachinery_Success() {
        ResponseEntity<Void> resp = controller.deleteMachinery(1L);
        verify(machineryService).deleteMachinery(1L);
        assertEquals(204, resp.getStatusCode().value());
    }

    @Test
    void tipoBusqueda_From() {
        assertEquals(AdminMachineryController.TipoBusqueda.NOMBRE,
                AdminMachineryController.TipoBusqueda.from("nombre"));

        assertEquals(AdminMachineryController.TipoBusqueda.CATEGORIA,
                AdminMachineryController.TipoBusqueda.from("  categoria "));
    }

    @Test
    void tipoBusqueda_From_Invalid_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> AdminMachineryController.TipoBusqueda.from("otro"));
    }

    @Test
    void buscarMaquinaria_BusquedaNombre() {
        Machinery maquinaria = new Machinery();
        when(machineryService.findByNameMachinery("Excavadora"))
                .thenReturn(List.of(maquinaria));

        String view = controller.buscarMaquinaria(
                " Excavadora ", null, "nombre", model);

        verify(model).addAttribute("maquinarias", List.of(maquinaria));
        assertEquals("search", view);
    }

    @Test
    void buscarMaquinaria_BusquedaCategoria() {
        Machinery maquinaria = new Machinery();
        when(machineryService.findByCategory("Agrícola"))
                .thenReturn(List.of(maquinaria));

        String view = controller.buscarMaquinaria(
                null, " Agrícola ", "categoria", model);

        verify(model).addAttribute("maquinarias", List.of(maquinaria));
        assertEquals("search", view);
    }

    @Test
    void buscarMaquinaria_NoParametros_DevuelveVacio() {
        String view = controller.buscarMaquinaria(
                null, null, "nombre", model);

        verify(model).addAttribute("maquinarias", List.of());
        assertEquals("search", view);
    }

    @Test
    void buscarMaquinaria_NombreVacio_DevuelveVacio() {
        String view = controller.buscarMaquinaria(
                "   ", null, "nombre", model);

        verify(model).addAttribute("maquinarias", List.of());
        assertEquals("search", view);
    }

    @Test
    void buscarMaquinaria_CategoriaVacia_DevuelveVacio() {
        String view = controller.buscarMaquinaria(
                null, "   ", "categoria", model);

        verify(model).addAttribute("maquinarias", List.of());
        assertEquals("search", view);
    }

    @Test
    void buscarMaquinaria_TipoNombre_ConCategoriaNoNull_DevuelveVacio() {
        String view = controller.buscarMaquinaria(
                null, "Agrícola", "nombre", model);

        verify(model).addAttribute("maquinarias", List.of());
        assertEquals("search", view);
    }

    @Test
    void buscarMaquinaria_TipoCategoria_ConNombreNoNull_DevuelveVacio() {
        String view = controller.buscarMaquinaria(
                "Excavadora", null, "categoria", model);

        verify(model).addAttribute("maquinarias", List.of());
        assertEquals("search", view);
    }

    @Test
    void createMachinery_WithNullImageFile_DefaultImageAssigned() {
        String result = controller.createMachinery(
                "Grúa", 5L, "Disponible",
                new BigDecimal("15000"), null);

        ArgumentCaptor<Machinery> captor = ArgumentCaptor.forClass(Machinery.class);
        verify(machineryService).createMachinery(captor.capture());

        assertEquals("/images/Case_IH_Axial-Flow.png", captor.getValue().getImageUrl());
        assertEquals("redirect:/home", result);
    }
}