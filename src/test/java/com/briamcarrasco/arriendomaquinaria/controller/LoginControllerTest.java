package com.briamcarrasco.arriendomaquinaria.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {

    private final LoginController controller = new LoginController();

    @Test
    void showLogin_returnsLoginView() {
        String result = controller.showLogin();
        assertEquals("login", result);
    }
}
