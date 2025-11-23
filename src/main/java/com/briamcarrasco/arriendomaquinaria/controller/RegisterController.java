package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.briamcarrasco.arriendomaquinaria.dto.RegisterRequestDto;
import com.briamcarrasco.arriendomaquinaria.service.UserService;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/**
 * Controlador para el registro de usuarios en el sistema.
 * Gestiona la ruta para mostrar la vista de registro de usuario y procesar el
 * registro.
 */
@Controller
public class RegisterController {

    private static final String REGISTER_VIEW = "register";

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Procesa el registro de un nuevo usuario.
     *
     * @param registerRequest datos del usuario a registrar
     * @param result          resultado de la validación
     * @param model           modelo para la vista
     * @return redirección a la página de login si el registro es exitoso, o la
     *         vista de registro con errores
     */
    @PostMapping("/register/user")
    public String registerUser(@Valid @ModelAttribute("registerRequest") RegisterRequestDto registerRequest,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return REGISTER_VIEW;
        }
        try {
            userService.createUser(registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    registerRequest.getEmail());
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return REGISTER_VIEW;
        }
    }

    /**
     * Muestra la página de registro de usuario.
     *
     * @param model modelo para la vista
     * @return nombre de la vista de registro
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequestDto());
        return REGISTER_VIEW;
    }

}