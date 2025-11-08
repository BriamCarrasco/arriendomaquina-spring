package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.service.MachineryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import java.util.Optional;

@Controller
@RequestMapping("/api/machinery")
public class AdminMachineryController {

    @Autowired
    private MachineryService machineryService;

    @PostMapping
    public String createMachinery(@ModelAttribute Machinery machinery) {
        machineryService.createMachinery(machinery);
        return "redirect:/home";
    }

    @GetMapping
    public ResponseEntity<List<Machinery>> findAll() {
        return ResponseEntity.ok(machineryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Machinery> findById(@PathVariable Long id) {
        Optional<Machinery> machinery = machineryService.findById(id);
        return machinery.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Machinery> updateMachinery(@PathVariable Long id, @RequestBody Machinery machinery) {
        try {
            Machinery updated = machineryService.updateMachinery(id, machinery);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMachinery(@PathVariable Long id) {
        machineryService.deleteMachinery(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/search")
    public String buscarMaquinaria(@RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam String tipo,
            Model model) {
        List<Machinery> maquinarias = List.of();
        if ("nombre".equals(tipo) && name != null && !name.isEmpty()) {
            maquinarias = machineryService.findByNameMachinery(name);
        } else if ("categoria".equals(tipo) && category != null && !category.isEmpty()) {
            maquinarias = machineryService.findByCategory(category);
        }
        model.addAttribute("maquinarias", maquinarias);
        return "search";
    }
}