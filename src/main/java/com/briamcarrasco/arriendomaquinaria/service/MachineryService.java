package com.briamcarrasco.arriendomaquinaria.service;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import java.util.Optional;

public interface MachineryService {
    Machinery createMachinery(Machinery machinery);

    Optional<Machinery> findById(Long id);

    Machinery updateMachinery(Long id, Machinery machinery);

    void deleteMachinery(Long id);
}