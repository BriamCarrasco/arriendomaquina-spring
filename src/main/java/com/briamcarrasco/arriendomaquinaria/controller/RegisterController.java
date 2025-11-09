package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para el registro de usuarios en el sistema.
 * Gestiona la ruta para mostrar la vista de registro de usuario.
 */
@Controller
public class RegisterController {

    /**
     * Muestra la página de registro de usuario.
     *
     * @return nombre de la vista de registro
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }

}