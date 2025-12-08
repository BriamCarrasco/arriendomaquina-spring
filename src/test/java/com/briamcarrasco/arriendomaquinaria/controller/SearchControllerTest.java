package com.briamcarrasco.arriendomaquinaria.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchControllerTest {

    private final ShearchController controller = new ShearchController();

    @Test
    void search_returnsSearchView() {
        String result = controller.search();
        assertEquals("search", result);
    }
}