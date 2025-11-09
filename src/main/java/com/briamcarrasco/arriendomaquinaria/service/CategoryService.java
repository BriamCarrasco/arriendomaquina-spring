package com.briamcarrasco.arriendomaquinaria.service;

import com.briamcarrasco.arriendomaquinaria.model.Category;
import java.util.List;

/**
 * Interfaz para el servicio de gestión de categorías en el sistema de arriendo
 * de maquinaria.
 * Define el método para obtener la lista de todas las categorías registradas.
 */
public interface CategoryService {

    /**
     * Obtiene todas las categorías registradas en el sistema.
     *
     * @return lista de objetos Category
     */
    List<Category> findAll();

}