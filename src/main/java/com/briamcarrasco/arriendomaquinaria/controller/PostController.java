package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class PostController {

    @GetMapping("/postmachinery")
    public String postMachinery() {
        return "postmachinery";
    }
    
}
