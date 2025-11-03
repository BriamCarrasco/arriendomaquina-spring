package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.repository.MachineryRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MachineryController {

    private final MachineryRepository repo;

    public MachineryController(MachineryRepository repo) { this.repo = repo; }

    @GetMapping("/inventario")
    public String home(Model model, @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("maquinarias", repo.findAll());
        // para tu <h1 th:text="|Bienvenido a ${name}!|">
        model.addAttribute("name", user != null ? user.getUsername() : "la plataforma");
        return "home";
    }
}
