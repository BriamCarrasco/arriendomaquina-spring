package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class HomeController {

    // 🚜 Landing pública (primera pantalla de la app)
    @GetMapping("/landing")
    public String landing(Authentication authentication) {
        // Si el usuario ya inició sesión, lo lleva a home
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        // Si no, muestra la página landing
        return "landing";
    }

    // 🏠 Página principal privada (requiere autenticación)
    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        if (authentication != null) {
            String username = authentication.getName();
            model.addAttribute("name", username);
        } else {
            model.addAttribute("name", "Invitado");
        }
        return "home";
    }

    // ⚙️ Redirección inicial al iniciar la app
    @GetMapping("/")
    public String redirectToLanding() {
        // Cuando se abre http://localhost:8080 → va al landing automáticamente
        return "redirect:/landing";
    }
}
