package com.briamcarrasco.arriendomaquinaria.service;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.repository.MachineryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.Optional;

@Service
public class MachineryServiceImpl implements MachineryService {

    @Autowired
    private MachineryRepository machineryRepository;

    @Override
    public Machinery createMachinery(Machinery machinery) {
        return machineryRepository.save(machinery);
    }

    @Override
    public Optional<Machinery> findById(Long id) {
        return machineryRepository.findById(id);
    }

    @Override
    public Machinery updateMachinery(Long id, Machinery machinery) {
        Optional<Machinery> existing = machineryRepository.findById(id);
        if (existing.isPresent()) {
            Machinery m = existing.get();
            m.setNameMachinery(machinery.getNameMachinery());
            m.setCategory(machinery.getCategory());
            m.setStatus(machinery.getStatus());
            m.setPricePerDay(machinery.getPricePerDay());
            return machineryRepository.save(m);
        }
        throw new RuntimeException("Machinery not found");
    }

    @Override
    public void deleteMachinery(Long id) {
        machineryRepository.deleteById(id);
    }

    @Override
    public List<Machinery> findAll() {
        return machineryRepository.findAll();
    }

    @Override
    public List<Machinery> findByNameMachinery(String nameMachinery) {
        return machineryRepository.findByNameMachineryContainingIgnoreCase(nameMachinery);
    }

    @Override
    public List<Machinery> findByCategory(String category) {
        return machineryRepository.findByCategoryIgnoreCase(category);
    }
}