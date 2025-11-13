package com.briamcarrasco.arriendomaquinaria.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.briamcarrasco.arriendomaquinaria.model.Category;
import com.briamcarrasco.arriendomaquinaria.repository.CategoryRepository;

/**
 * Implementación del servicio para la gestión de categorías en el sistema de
 * arriendo de maquinaria.
 * Proporciona métodos para obtener la lista de todas las categorías
 * registradas.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Obtiene todas las categorías registradas en el sistema.
     *
     * @return lista de objetos Category
     */
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

}