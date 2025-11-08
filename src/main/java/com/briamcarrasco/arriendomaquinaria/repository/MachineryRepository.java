package com.briamcarrasco.arriendomaquinaria.repository;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MachineryRepository extends JpaRepository<Machinery, Long> {

    boolean existsByNameMachinery(String nameMachinery);

    Machinery findByNameMachinery(String nameMachinery);
    
    List<Machinery> findByNameMachineryContainingIgnoreCase(String nameMachinery);

    List<Machinery> findByCategory_NameIgnoreCase(String name);
}