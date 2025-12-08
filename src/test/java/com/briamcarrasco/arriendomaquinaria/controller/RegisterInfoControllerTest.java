package com.briamcarrasco.arriendomaquinaria.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterInfoControllerTest {

    private final RegisterInfoController controller = new RegisterInfoController();

    @Test
    void registerinfouser_returnsRegisterInfoUserView() {
        String result = controller.registerinfouser();
        assertEquals("registerinfouser", result);
    }
}
