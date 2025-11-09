package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la autenticación de usuarios en el sistema.
 * Gestiona la ruta para mostrar la vista de inicio de sesión.
 */
@Controller
public class LoginController {

    /**
     * Muestra la página de inicio de sesión.
     *
     * @return nombre de la vista de login
     */
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

}