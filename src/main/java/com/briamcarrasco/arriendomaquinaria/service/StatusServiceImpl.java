package com.briamcarrasco.arriendomaquinaria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.briamcarrasco.arriendomaquinaria.repository.StatusRepository;
import com.briamcarrasco.arriendomaquinaria.model.Status;
import java.util.List;

/**
 * Implementación del servicio para la gestión de estados en el sistema de
 * arriendo de maquinaria.
 * Proporciona métodos para obtener la lista de todos los estados registrados.
 *
 */
@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    private StatusRepository statusRepository;

    /**
     * Obtiene todos los estados registrados en el sistema.
     *
     * @return lista de objetos Status
     */
    @Override
    public List<Status> findAll() {
        return statusRepository.findAll();
    }

}