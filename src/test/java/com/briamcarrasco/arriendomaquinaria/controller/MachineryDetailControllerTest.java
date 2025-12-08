package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.model.Review;
import com.briamcarrasco.arriendomaquinaria.service.MachineryService;
import com.briamcarrasco.arriendomaquinaria.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MachineryDetailControllerTest {

    @Mock
    private MachineryService machineryService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private Model model;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private MachineryDetailController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------------------------------------------------
    // MACHINERY DETAIL (AUTENTICADO)
    // -------------------------------------------------------------

    @Test
    void machineryDetail_MachineryFound_WithAuthentication_WithQueryString() {
        Machinery machinery = new Machinery();
        machinery.setId(1L);
        List<Review> reviews = List.of(new Review());

        when(machineryService.findById(1L)).thenReturn(Optional.of(machinery));
        when(reviewService.getReviewsByMachinery(1L)).thenReturn(reviews);
        when(reviewService.getAverageRating(1L)).thenReturn(4.5);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/machinerydetail"));
        when(request.getQueryString()).thenReturn("id=1");
        when(authentication.getName()).thenReturn("testUser");

        String result = controller.machineryDetail(1L, authentication, model, request);

        assertEquals("machinerydetail", result);
        verify(model).addAttribute("machinery", machinery);
        verify(model).addAttribute("reviews", reviews);
        verify(model).addAttribute("averageRating", 4.5);
        verify(model).addAttribute("currentUrl", "http://localhost/machinerydetail?id=1");
        verify(model).addAttribute("name", "testUser");
    }

    @Test
    void machineryDetail_MachineryFound_WithAuthentication_NoQueryString() {
        Machinery machinery = new Machinery();
        machinery.setId(2L);
        List<Review> reviews = List.of();

        when(machineryService.findById(2L)).thenReturn(Optional.of(machinery));
        when(reviewService.getReviewsByMachinery(2L)).thenReturn(reviews);
        when(reviewService.getAverageRating(2L)).thenReturn(3.0);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/machinerydetail"));
        when(request.getQueryString()).thenReturn(null);
        when(authentication.getName()).thenReturn("adminUser");

        String result = controller.machineryDetail(2L, authentication, model, request);

        assertEquals("machinerydetail", result);
        verify(model).addAttribute("currentUrl", "http://localhost/machinerydetail");
        verify(model).addAttribute("name", "adminUser");
    }

    @Test
    void machineryDetail_MachineryFound_NoAuthentication() {
        Machinery machinery = new Machinery();
        machinery.setId(3L);

        when(machineryService.findById(3L)).thenReturn(Optional.of(machinery));
        when(reviewService.getReviewsByMachinery(3L)).thenReturn(List.of());
        when(reviewService.getAverageRating(3L)).thenReturn(null);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/machinerydetail"));
        when(request.getQueryString()).thenReturn("id=3");

        String result = controller.machineryDetail(3L, null, model, request);

        assertEquals("machinerydetail", result);
        verify(model).addAttribute("name", "Invitado");
    }

    @Test
    void machineryDetail_MachineryNotFound() {
        when(machineryService.findById(99L)).thenReturn(Optional.empty());

        String result = controller.machineryDetail(99L, authentication, model, request);

        assertEquals("machinery-not-found", result);
        verify(model).addAttribute("machinery", null);
        verifyNoInteractions(reviewService);
    }

    // -------------------------------------------------------------
    // PUBLIC MACHINERY DETAIL
    // -------------------------------------------------------------

    @Test
    void publicMachineryDetail_MachineryFound_WithQueryString() {
        Machinery machinery = new Machinery();
        machinery.setId(1L);
        List<Review> reviews = List.of(new Review());

        when(machineryService.findById(1L)).thenReturn(Optional.of(machinery));
        when(reviewService.getReviewsByMachinery(1L)).thenReturn(reviews);
        when(reviewService.getAverageRating(1L)).thenReturn(4.0);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/public/machinerydetail"));
        when(request.getQueryString()).thenReturn("id=1");

        String result = controller.publicMachineryDetail(1L, model, request);

        assertEquals("machinerydetail", result);
        verify(model).addAttribute("machinery", machinery);
        verify(model).addAttribute("reviews", reviews);
        verify(model).addAttribute("averageRating", 4.0);
        verify(model).addAttribute("currentUrl", "http://localhost/public/machinerydetail?id=1");
        verify(model).addAttribute("name", "Visitante");
    }

    @Test
    void publicMachineryDetail_MachineryFound_NoQueryString() {
        Machinery machinery = new Machinery();
        machinery.setId(5L);

        when(machineryService.findById(5L)).thenReturn(Optional.of(machinery));
        when(reviewService.getReviewsByMachinery(5L)).thenReturn(List.of());
        when(reviewService.getAverageRating(5L)).thenReturn(0.0);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/public/machinerydetail"));
        when(request.getQueryString()).thenReturn(null);

        String result = controller.publicMachineryDetail(5L, model, request);

        assertEquals("machinerydetail", result);
        verify(model).addAttribute("currentUrl", "http://localhost/public/machinerydetail");
        verify(model).addAttribute("name", "Visitante");
    }

    @Test
    void publicMachineryDetail_MachineryNotFound() {
        when(machineryService.findById(100L)).thenReturn(Optional.empty());

        String result = controller.publicMachineryDetail(100L, model, request);

        assertEquals("machinery-not-found", result);
        verify(model).addAttribute("machinery", null);
        verifyNoInteractions(reviewService);
    }
}
