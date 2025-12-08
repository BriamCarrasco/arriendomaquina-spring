package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.repository.MachineryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MachineryControllerTest {

    @Mock
    private MachineryRepository repo;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Model model;

    @InjectMocks
    private MachineryController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void home_withUser_returnsHome() {
        List<Machinery> machines = List.of(new Machinery());
        when(repo.findAll()).thenReturn(machines);
        when(userDetails.getUsername()).thenReturn("testUser");

        String result = controller.home(model, userDetails);

        assertEquals("home", result);
        verify(model).addAttribute("maquinarias", machines);
        verify(model).addAttribute("name", "testUser");
    }

    @Test
    void home_noUser_returnsHomeWithDefaultName() {
        List<Machinery> machines = List.of();
        when(repo.findAll()).thenReturn(machines);

        String result = controller.home(model, null);

        assertEquals("home", result);
        verify(model).addAttribute("name", "la plataforma");
    }
}
