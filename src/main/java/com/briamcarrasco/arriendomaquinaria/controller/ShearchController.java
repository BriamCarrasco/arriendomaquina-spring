package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShearchController {
    
    @GetMapping("/search")
    public String search() {
        return "search";
    }
}
