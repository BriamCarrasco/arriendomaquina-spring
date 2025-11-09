package com.briamcarrasco.arriendomaquinaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.briamcarrasco.arriendomaquinaria.model.MachineryRental;

/**
 * Repositorio para la entidad MachineryRental.
 * Proporciona métodos para acceder y gestionar los arriendos de maquinaria en
 * la base de datos.
 */
public interface MachineryRentalRepository extends JpaRepository<MachineryRental, Long> {
}