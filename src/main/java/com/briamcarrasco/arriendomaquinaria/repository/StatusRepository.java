package com.briamcarrasco.arriendomaquinaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.briamcarrasco.arriendomaquinaria.model.Status;

/**
 * Repositorio para la entidad Status.
 * Proporciona métodos para acceder y gestionar los estados en la base de datos.
 */
public interface StatusRepository extends JpaRepository<Status, Long> {

    /**
     * Busca un estado por su nombre.
     *
     * @param name nombre del estado
     * @return el estado encontrado o null si no existe
     */
    Status findByName(String name);

    /**
     * Verifica si existe un estado con el nombre especificado.
     *
     * @param name nombre del estado
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
}