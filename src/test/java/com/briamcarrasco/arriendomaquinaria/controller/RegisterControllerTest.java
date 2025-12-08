package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.dto.RegisterRequestDto;
import com.briamcarrasco.arriendomaquinaria.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private RegisterController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_withValidationErrors_returnsRegisterView() {
        when(bindingResult.hasErrors()).thenReturn(true);
        RegisterRequestDto request = new RegisterRequestDto();

        String result = controller.registerUser(request, bindingResult, model);

        assertEquals("register", result);
    }

    @Test
    void registerUser_success_redirectsToLogin() {
        when(bindingResult.hasErrors()).thenReturn(false);
        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("newUser");
        request.setPassword("pass123");
        request.setEmail("new@email.com");

        String result = controller.registerUser(request, bindingResult, model);

        assertEquals("redirect:/login?registered", result);
        verify(userService).createUser("newUser", "pass123", "new@email.com");
    }

    @Test
    void registerUser_serviceThrows_returnsRegisterWithError() {
        when(bindingResult.hasErrors()).thenReturn(false);
        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("existingUser");
        request.setPassword("pass");
        request.setEmail("exist@email.com");
        doThrow(new IllegalArgumentException("Usuario ya existe")).when(userService)
                .createUser(anyString(), anyString(), anyString());

        String result = controller.registerUser(request, bindingResult, model);

        assertEquals("register", result);
        verify(model).addAttribute(eq("error"), anyString());
    }

    @Test
    void register_returnsRegisterView() {
        String result = controller.register(model);

        assertEquals("register", result);
        verify(model).addAttribute(eq("registerRequest"), any(RegisterRequestDto.class));
    }
}
