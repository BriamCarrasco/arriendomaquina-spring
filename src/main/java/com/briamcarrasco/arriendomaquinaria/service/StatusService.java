package com.briamcarrasco.arriendomaquinaria.service;

import com.briamcarrasco.arriendomaquinaria.model.Status;
import java.util.List;

/**
 * Interfaz para el servicio de gestión de estados en el sistema de arriendo de
 * maquinaria.
 * 
 * Define el contrato para obtener la lista de todos los estados registrados.
 */
public interface StatusService {

    /**
     * Obtiene todos los estados registrados en el sistema.
     *
     * @return lista de objetos Status
     */
    List<Status> findAll();

}