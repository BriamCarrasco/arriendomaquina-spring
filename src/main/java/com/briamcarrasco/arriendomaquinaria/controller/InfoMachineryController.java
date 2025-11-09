package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para mostrar la información adicional de una maquinaria en el
 * sistema.
 * Gestiona la ruta para mostrar la vista de detalles de maquinaria.
 */
@Controller
public class InfoMachineryController {

    /**
     * Muestra la página de información adicional de maquinaria.
     *
     * @return nombre de la vista de información de maquinaria
     */
    @GetMapping("/infomachinery")
    public String infoMachinery() {
        return "infomachinery";
    }
}