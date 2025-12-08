package com.briamcarrasco.arriendomaquinaria.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import com.briamcarrasco.arriendomaquinaria.model.Category;
import com.briamcarrasco.arriendomaquinaria.repository.CategoryRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void findAll_returnsListFromRepository() {
        Category c1 = new Category();
        Category c2 = new Category();
        List<Category> list = Arrays.asList(c1, c2);
        when(categoryRepository.findAll()).thenReturn(list);

        List<Category> result = categoryService.findAll();

        assertSame(list, result);
        assertEquals(2, result.size());
        verify(categoryRepository).findAll();
    }
}