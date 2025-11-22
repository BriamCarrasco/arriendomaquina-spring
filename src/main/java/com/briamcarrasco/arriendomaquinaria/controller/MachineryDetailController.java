package com.briamcarrasco.arriendomaquinaria.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.model.Review;
import com.briamcarrasco.arriendomaquinaria.service.MachineryService;
import com.briamcarrasco.arriendomaquinaria.service.ReviewService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador para mostrar el detalle de una maquinaria y sus reseñas.
 */
@Controller
public class MachineryDetailController {

    private static final Logger logger = LoggerFactory.getLogger(MachineryDetailController.class);

    private final MachineryService machineryService;
    private final ReviewService reviewService;

    public MachineryDetailController(MachineryService machineryService, ReviewService reviewService) {
        this.machineryService = machineryService;
        this.reviewService = reviewService;
    }

    @GetMapping("/machinerydetail")
    public String machineryDetail(@RequestParam(name = "id") Long id,
            Authentication authentication,
            Model model,
            HttpServletRequest request) {

        Machinery machinery = machineryService.findById(id).orElse(null);
        if (machinery == null) {
            logger.warn("Maquinaria no encontrada id={}", id);
            model.addAttribute("machinery", null);
            return "machinerydetail";
        }

        List<Review> reviews = reviewService.getReviewsByMachinery(id);
        Double averageRating = reviewService.getAverageRating(id);

        String currentUrl = request.getRequestURL().toString()
                + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        model.addAttribute("machinery", machinery);
        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("currentUrl", currentUrl);
        model.addAttribute("name", authentication != null ? authentication.getName() : "Invitado");
        return "machinerydetail";
    }

    // Endpoint público para compartir sin requerir autenticación
    @GetMapping("/public/machinerydetail")
    public String publicMachineryDetail(@RequestParam(name = "id") Long id,
            Model model,
            HttpServletRequest request) {
        Machinery machinery = machineryService.findById(id).orElse(null);
        if (machinery == null) {
            model.addAttribute("machinery", null);
            return "machinerydetail";
        }
        List<Review> reviews = reviewService.getReviewsByMachinery(id);
        Double averageRating = reviewService.getAverageRating(id);
        String currentUrl = request.getRequestURL().toString()
                + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        model.addAttribute("machinery", machinery);
        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("currentUrl", currentUrl);
        // nombre genérico, no autenticado
        model.addAttribute("name", "Visitante");
        return "machinerydetail";
    }
}