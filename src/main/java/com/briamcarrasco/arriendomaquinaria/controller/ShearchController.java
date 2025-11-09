package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la búsqueda de maquinarias en el sistema.
 * Gestiona la ruta para mostrar la vista de búsqueda.
 */
@Controller
public class ShearchController {

    /**
     * Muestra la página de búsqueda de maquinarias.
     *
     * @return nombre de la vista de búsqueda
     */
    @GetMapping("/search")
    public String search() {
        return "search";
    }
}