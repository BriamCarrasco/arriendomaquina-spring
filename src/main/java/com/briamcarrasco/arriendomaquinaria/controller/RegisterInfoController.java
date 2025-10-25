package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class RegisterInfoController {
    @GetMapping("/registerinfouser")
    public String registerinfouser() {
        return "registerinfouser";
    }
}
