package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.service.MachineryService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador para mostrar el detalle de una maquinaria.
 * Solo usuarios autenticados pueden acceder a la página de detalle.
 */
@Controller
public class MachineryDetailController {

    private final MachineryService machineryService;

    public MachineryDetailController(MachineryService machineryService) {
        this.machineryService = machineryService;
    }

    /**
     * Muestra la página de detalle de una maquinaria específica.
     *
     * @param authentication información de autenticación del usuario
     * @param model          modelo para la vista
     * @param id             identificador de la maquinaria
     * @return nombre de la vista de detalle de maquinaria
     */
    @GetMapping("/machinerydetail")
    public String machineryDetail(
            Authentication authentication,
            Model model,
            @RequestParam("id") Long id,
            HttpServletRequest request // <-- Agrega este parámetro
    ) {
        Machinery machinery = machineryService.findById(id).orElse(null);
        model.addAttribute("machinery", machinery);
        model.addAttribute("name", authentication != null ? authentication.getName() : "Invitado");
        String url = request.getRequestURL().toString()
                + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        model.addAttribute("currentUrl", url);
        return "machinerydetail";
    }

        /**
     * Muestra la página pública de detalle de una maquinaria para compartir.
     * Accesible sin autenticación.
     */
        @GetMapping("/public/machinerydetail")
        public String publicMachineryDetail(
                Model model,
                @RequestParam("id") Long id,
                HttpServletRequest request) {
            Machinery machinery = machineryService.findById(id).orElse(null);
            model.addAttribute("machinery", machinery);
            String url = request.getRequestURL().toString()
                    + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            model.addAttribute("currentUrl", url);
            return "machinerydetail"; // Reusa la misma plantilla, pero sin elementos de autenticación
        }
}