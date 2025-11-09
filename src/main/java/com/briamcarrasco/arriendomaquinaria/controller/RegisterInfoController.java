package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para el registro de información adicional de usuarios en el
 * sistema.
 * Gestiona la ruta para mostrar la vista de registro de información de usuario.
 */
@Controller
public class RegisterInfoController {

    /**
     * Muestra la página de registro de información adicional de usuario.
     *
     * @return nombre de la vista de registro de información de usuario
     */
    @GetMapping("/registerinfouser")
    public String registerinfouser() {
        return "registerinfouser";
    }
}