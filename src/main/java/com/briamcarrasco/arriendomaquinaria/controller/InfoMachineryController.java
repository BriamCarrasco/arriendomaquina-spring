package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoMachineryController {

    @GetMapping("/infomachinery")
    public String infoMachinery() {
        return "infomachinery";
    }
}
