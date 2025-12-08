package com.briamcarrasco.arriendomaquinaria.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminPanelControllerTest {

    private final AdminPanelController controller = new AdminPanelController();

    @Test
    void adminpanel_returnsRedirectToUsers() {
        String result = controller.adminpanel();
        assertEquals("redirect:/api/admin/users", result);
    }
}
