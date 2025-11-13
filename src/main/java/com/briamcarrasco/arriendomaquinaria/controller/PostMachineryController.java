package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.briamcarrasco.arriendomaquinaria.service.CategoryService;
import com.briamcarrasco.arriendomaquinaria.service.StatusService;

/**
 * Controlador para la publicación de maquinarias en el sistema.
 * Gestiona la ruta para mostrar la vista de publicación y carga las categorías
 * y estados disponibles.
 */
@Controller
public class PostMachineryController {

    private final CategoryService categoryService;

    private final StatusService statusService;

    public PostMachineryController(
            CategoryService categoryService,
            StatusService statusService) {
        this.categoryService = categoryService;
        this.statusService = statusService;
    }

    /**
     * Muestra la página de publicación de maquinaria.
     * Agrega al modelo las categorías y estados disponibles para la selección.
     *
     * @param model modelo para la vista
     * @return nombre de la vista de publicación de maquinaria
     */
    @GetMapping("/postmachinery")
    public String postMachinery(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("statuses", statusService.findAll());
        return "postmachinery";
    }

}