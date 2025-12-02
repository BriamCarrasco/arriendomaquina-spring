package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPanelController {

    @GetMapping("/adminpanel")
    public String adminpanel() {
        // Redirigimos al controlador de usuarios para que cargue la lista
        return "redirect:/api/admin/users";
    }

}
