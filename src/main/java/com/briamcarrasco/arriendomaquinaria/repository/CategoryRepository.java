package com.briamcarrasco.arriendomaquinaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.briamcarrasco.arriendomaquinaria.model.Category;

/**
 * Repositorio para la entidad Category.
 * Proporciona métodos para acceder y gestionar las categorías en la base de
 * datos.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Busca una categoría por su nombre.
     *
     * @param name nombre de la categoría
     * @return la categoría encontrada o null si no existe
     */
    Category findByName(String name);

    /**
     * Verifica si existe una categoría con el nombre especificado.
     *
     * @param name nombre de la categoría
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
}