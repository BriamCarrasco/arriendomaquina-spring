package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.model.Category;
import com.briamcarrasco.arriendomaquinaria.model.Status;
import com.briamcarrasco.arriendomaquinaria.service.CategoryService;
import com.briamcarrasco.arriendomaquinaria.service.StatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostMachineryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private StatusService statusService;

    @Mock
    private Model model;

    @InjectMocks
    private PostMachineryController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void postMachinery_returnsPostMachineryView() {
        List<Category> categories = List.of(new Category());
        List<Status> statuses = List.of(new Status());
        when(categoryService.findAll()).thenReturn(categories);
        when(statusService.findAll()).thenReturn(statuses);

        String result = controller.postMachinery(model);

        assertEquals("postmachinery", result);
        verify(model).addAttribute("categories", categories);
        verify(model).addAttribute("statuses", statuses);
    }
}