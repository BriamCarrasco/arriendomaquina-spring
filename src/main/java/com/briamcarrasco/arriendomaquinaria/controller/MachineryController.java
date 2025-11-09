package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.repository.MachineryRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la gestión del inventario de maquinarias en el sistema.
 * Permite mostrar la lista de maquinarias disponibles y el nombre del usuario
 * autenticado.
 */
@Controller
public class MachineryController {

    private final MachineryRepository repo;

    /**
     * Constructor que recibe el repositorio de maquinarias.
     *
     * @param repo repositorio de maquinarias
     */
    public MachineryController(MachineryRepository repo) {
        this.repo = repo;
    }

    /**
     * Muestra la página de inventario de maquinarias.
     * Agrega al modelo la lista de maquinarias y el nombre del usuario autenticado.
     *
     * @param model modelo para la vista
     * @param user  usuario autenticado
     * @return nombre de la vista de inventario
     */
    @GetMapping("/inventario")
    public String home(Model model, @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("maquinarias", repo.findAll());
        model.addAttribute("name", user != null ? user.getUsername() : "la plataforma");
        return "home";
    }
}