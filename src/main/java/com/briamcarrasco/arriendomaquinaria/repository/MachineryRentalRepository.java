package com.briamcarrasco.arriendomaquinaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.briamcarrasco.arriendomaquinaria.model.MachineryRental;

public interface MachineryRentalRepository extends JpaRepository<MachineryRental, Long> {
}