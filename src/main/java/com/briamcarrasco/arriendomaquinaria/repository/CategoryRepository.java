package com.briamcarrasco.arriendomaquinaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.briamcarrasco.arriendomaquinaria.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    boolean existsByName(String name);
}