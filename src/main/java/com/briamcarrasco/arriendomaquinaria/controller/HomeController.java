package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.repository.MachineryRepository; // 👈 import necesario
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class HomeController {

    private final MachineryRepository repo; 

    public HomeController(MachineryRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/landing")
    public String landing(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        return "landing";
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
    System.out.println("Authentication in /home: " + authentication);
        model.addAttribute("name", authentication != null ? authentication.getName() : "Invitado");
        model.addAttribute("maquinarias", repo.findAll()); 
        return "home";
    }

    @GetMapping("/")
    public String redirectToLanding() {
        return "redirect:/landing";
    }
}
