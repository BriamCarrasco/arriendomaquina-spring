package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.briamcarrasco.arriendomaquinaria.service.CategoryService;
import com.briamcarrasco.arriendomaquinaria.service.StatusService;

@Controller
public class PostMachineryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StatusService statusService;

    @GetMapping("/postmachinery")
    public String postMachinery(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("statuses", statusService.findAll());
        return "postmachinery";
    }

}
