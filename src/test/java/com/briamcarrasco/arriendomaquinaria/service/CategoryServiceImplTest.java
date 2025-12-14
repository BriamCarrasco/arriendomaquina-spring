package com.briamcarrasco.arriendomaquinaria.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.briamcarrasco.arriendomaquinaria.model.Category;
import com.briamcarrasco.arriendomaquinaria.repository.CategoryRepository;

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

    @Test
    void findAll_whenRepositoryReturnsEmpty_returnsEmptyList() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        List<Category> result = categoryService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryRepository).findAll();
    }

    @Test
    void findAll_whenRepositoryReturnsNull_handlesGracefully() {
        when(categoryRepository.findAll()).thenReturn(null);

        List<Category> result = categoryService.findAll();

        assertNull(result);
        verify(categoryRepository).findAll();
    }

    @Test
    void findAll_verifyRepositoryIsCalledOnce() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        categoryService.findAll();

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void findAll_whenRepositoryThrowsException_propagatesException() {
        when(categoryRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        Exception ex = assertThrows(RuntimeException.class, () -> {
            categoryService.findAll();
        });
        assertNotNull(ex);
    }

    @Test
    void findAll_returnsListWithMultipleCategories() {
        List<Category> categories = Arrays.asList(
            new Category(), new Category(), new Category()
        );
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.findAll();

        assertEquals(3, result.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void findAll_returnsActualRepositoryList() {
        Category c1 = new Category();
        c1.setName("Categoría 1");
        List<Category> list = Arrays.asList(c1);
        when(categoryRepository.findAll()).thenReturn(list);

        List<Category> result = categoryService.findAll();

        assertEquals("Categoría 1", result.get(0).getName());
    }
}