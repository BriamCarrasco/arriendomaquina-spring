package com.briamcarrasco.arriendomaquinaria.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

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
    void findAll_whenRepositoryHasCategories_returnsList() {
        Category c1 = mock(Category.class);
        Category c2 = mock(Category.class);

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Category> result = categoryService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void findAll_whenRepositoryReturnsEmptyList_returnsEmptyList() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<Category> result = categoryService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void findAll_whenRepositoryHasSingleCategory_returnsThatCategory() {
        Category c1 = mock(Category.class);

        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(c1));

        List<Category> result = categoryService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(c1, result.get(0));
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void findAll_returnsSameListInstance_fromRepository() {
        ArrayList<Category> list = new ArrayList<>();
        Category c = mock(Category.class);
        list.add(c);

        when(categoryRepository.findAll()).thenReturn(list);

        List<Category> result = categoryService.findAll();

        assertSame(list, result, "The service should return the same List instance provided by the repository");
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void findAll_whenRepositoryThrowsRuntimeException_propagatesException() {
        RuntimeException ex = new RuntimeException("DB error");
        when(categoryRepository.findAll()).thenThrow(ex);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> categoryService.findAll());
        assertEquals("DB error", thrown.getMessage());
        verify(categoryRepository, times(1)).findAll();
    }
}