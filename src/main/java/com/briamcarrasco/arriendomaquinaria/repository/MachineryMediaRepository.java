package com.briamcarrasco.arriendomaquinaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.briamcarrasco.arriendomaquinaria.model.MachineryMedia;

public interface MachineryMediaRepository extends JpaRepository<MachineryMedia, Long> {
    List<MachineryMedia> findByMachineryId(Long machineryId);
}
