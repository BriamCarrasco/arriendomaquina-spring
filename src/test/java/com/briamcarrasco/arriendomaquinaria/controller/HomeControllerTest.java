package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.repository.MachineryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HomeControllerTest {

    @Mock
    private MachineryRepository repo;

    @Mock
    private Authentication authentication;

    @Mock
    private Model model;

    @InjectMocks
    private HomeController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void landing_authenticated_redirectsToHome() {
        when(authentication.isAuthenticated()).thenReturn(true);

        String result = controller.landing(authentication);

        assertEquals("redirect:/home", result);
    }

    @Test
    void landing_notAuthenticated_returnsLanding() {
        when(authentication.isAuthenticated()).thenReturn(false);

        String result = controller.landing(authentication);

        assertEquals("landing", result);
    }

    @Test
    void landing_nullAuthentication_returnsLanding() {
        String result = controller.landing(null);

        assertEquals("landing", result);
    }

    @Test
    void home_withAuthentication_returnsHome() {
        List<Machinery> machines = List.of(new Machinery());
        when(authentication.getName()).thenReturn("testUser");
        when(repo.findAll()).thenReturn(machines);

        String result = controller.home(authentication, model);

        assertEquals("home", result);
        verify(model).addAttribute("name", "testUser");
        verify(model).addAttribute("maquinarias", machines);
    }

    @Test
    void home_noAuthentication_returnsHomeWithInvitado() {
        List<Machinery> machines = List.of();
        when(repo.findAll()).thenReturn(machines);

        String result = controller.home(null, model);

        assertEquals("home", result);
        verify(model).addAttribute("name", "Invitado");
    }

    @Test
    void redirectToLanding_returnsRedirect() {
        String result = controller.redirectToLanding();

        assertEquals("redirect:/landing", result);
    }
}
