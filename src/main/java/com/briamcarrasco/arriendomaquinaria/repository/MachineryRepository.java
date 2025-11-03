package com.briamcarrasco.arriendomaquinaria.repository;
import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineryRepository extends JpaRepository<Machinery, Long> { }