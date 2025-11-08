package com.briamcarrasco.arriendomaquinaria.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.briamcarrasco.arriendomaquinaria.model.Category;
import com.briamcarrasco.arriendomaquinaria.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

}
