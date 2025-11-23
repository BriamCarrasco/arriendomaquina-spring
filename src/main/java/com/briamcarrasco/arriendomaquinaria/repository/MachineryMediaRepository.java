package com.briamcarrasco.arriendomaquinaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.briamcarrasco.arriendomaquinaria.model.MachineryMedia;

/**
 * Repositorio para la entidad MachineryMedia.
 * Proporciona métodos para acceder y gestionar archivos multimedia asociados a
 * maquinarias en la base de datos.
 */
public interface MachineryMediaRepository extends JpaRepository<MachineryMedia, Long> {
    /**
     * Obtiene la lista de archivos multimedia asociados a una maquinaria.
     *
     * @param machineryId identificador de la maquinaria
     * @return lista de objetos MachineryMedia
     */
    List<MachineryMedia> findByMachineryId(Long machineryId);
}