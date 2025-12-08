package com.briamcarrasco.arriendomaquinaria.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InfoMachineryControllerTest {

    private final InfoMachineryController controller = new InfoMachineryController();

    @Test
    void infoMachinery_returnsInfoMachineryView() {
        String result = controller.infoMachinery();
        assertEquals("infomachinery", result);
    }
}
