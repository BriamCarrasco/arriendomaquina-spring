package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.service.MachineryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/machinery")
public class AdminMachineryController {

    @Autowired
    private MachineryService machineryService;

    @PostMapping
    public ResponseEntity<Machinery> createMachinery(@RequestBody Machinery machinery) {
        Machinery created = machineryService.createMachinery(machinery);
        return ResponseEntity.ok(created);
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
}