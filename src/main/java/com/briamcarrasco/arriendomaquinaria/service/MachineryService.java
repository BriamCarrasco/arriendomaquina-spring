package com.briamcarrasco.arriendomaquinaria.service;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import java.util.Optional;
import java.util.List;

/**
 * Interfaz para el servicio de gestión de maquinaria en el sistema de arriendo.
 * Define los métodos para crear, buscar, actualizar, eliminar y listar
 * maquinarias.
 */
public interface MachineryService {

    /**
     * Crea una nueva maquinaria en el sistema.
     *
     * @param machinery objeto Machinery a crear
     * @return la maquinaria creada
     */
    Machinery createMachinery(Machinery machinery);

    /**
     * Busca una maquinaria por su identificador.
     *
     * @param id identificador de la maquinaria
     * @return un Optional con la maquinaria encontrada o vacío si no existe
     */
    Optional<Machinery> findById(Long id);

    /**
     * Actualiza los datos de una maquinaria existente.
     *
     * @param id        identificador de la maquinaria a actualizar
     * @param machinery datos actualizados de la maquinaria
     * @return la maquinaria actualizada
     */
    Machinery updateMachinery(Long id, Machinery machinery);

    /**
     * Elimina una maquinaria por su identificador.
     *
     * @param id identificador de la maquinaria a eliminar
     */
    void deleteMachinery(Long id);

    /**
     * Obtiene la lista de todas las maquinarias registradas.
     *
     * @return lista de objetos Machinery
     */
    List<Machinery> findAll();

    /**
     * Busca maquinarias por nombre, ignorando mayúsculas y minúsculas.
     *
     * @param nameMachinery nombre de la maquinaria a buscar
     * @return lista de maquinarias que coinciden con el nombre
     */
    List<Machinery> findByNameMachinery(String nameMachinery);

    /**
     * Busca maquinarias por categoría, ignorando mayúsculas y minúsculas.
     *
     * @param name nombre de la categoría
     * @return lista de maquinarias que pertenecen a la categoría
     */
    List<Machinery> findByCategory(String name);
}