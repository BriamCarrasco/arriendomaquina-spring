package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostMachineryController {

    @GetMapping("/postmachinery")
    public String postMachinery() {
        return "postmachinery";
    }
    
}
