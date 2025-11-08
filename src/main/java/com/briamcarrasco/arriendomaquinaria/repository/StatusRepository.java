package com.briamcarrasco.arriendomaquinaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.briamcarrasco.arriendomaquinaria.model.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Status findByName(String name);

    boolean existsByName(String name);
}