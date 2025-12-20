package com.briamcarrasco.arriendomaquinaria.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    private Status status;

    @BeforeEach
    void setUp() {
        status = new Status();
    }

    @Test
    void testGetSetId() {
        Long id = 1L;
        status.setId(id);
        assertEquals(id, status.getId());
    }

    @Test
    void testGetSetName() {
        String name = "Disponible";
        status.setName(name);
        assertEquals(name, status.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        Status status1 = new Status();
        status1.setId(1L);
        status1.setName("Disponible");

        Status status2 = new Status();
        status2.setId(1L);
        status2.setName("Disponible");

        assertEquals(status1, status2);
        assertEquals(status1.hashCode(), status2.hashCode());
    }

    @Test
    void testNotEquals() {
        Status status1 = new Status();
        status1.setId(1L);
        status1.setName("Disponible");

        Status status2 = new Status();
        status2.setId(2L);
        status2.setName("En Mantenimiento");

        assertNotEquals(status1, status2);
    }

    @Test
    void testToString() {
        status.setId(1L);
        status.setName("Operativa");

        String toString = status.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Status"));
    }

    @Test
    void testCreateWithAllFields() {
        Status testStatus = new Status();
        testStatus.setId(5L);
        testStatus.setName("Arrendada");

        assertEquals(5L, testStatus.getId());
        assertEquals("Arrendada", testStatus.getName());
    }

    @Test
    void testDifferentStatusNames() {
        Status disponible = new Status();
        disponible.setId(1L);
        disponible.setName("Disponible");

        Status mantenimiento = new Status();
        mantenimiento.setId(2L);
        mantenimiento.setName("En Mantenimiento");

        Status arrendada = new Status();
        arrendada.setId(3L);
        arrendada.setName("Arrendada");

        assertNotEquals(disponible.getName(), mantenimiento.getName());
        assertNotEquals(mantenimiento.getName(), arrendada.getName());
        assertNotEquals(disponible.getId(), arrendada.getId());
    }

    @Test
    void testEmptyName() {
        status.setName("");
        assertEquals("", status.getName());
        assertTrue(status.getName().isEmpty());
    }

    @Test
    void testNameWithSpaces() {
        String nameWithSpaces = "Fuera de Servicio";
        status.setName(nameWithSpaces);
        assertEquals(nameWithSpaces, status.getName());
        assertTrue(status.getName().contains(" "));
    }

    @Test
    void testLongName() {
        String longName = "Estado de la Maquinaria en Proceso de Revisión Técnica Extendida";
        status.setName(longName);
        assertEquals(longName, status.getName());
    }

    @Test
    void testNameWithSpecialCharacters() {
        String specialName = "En Revisión - Pendiente";
        status.setName(specialName);
        assertEquals(specialName, status.getName());
    }

    @Test
    void testInitialValues() {
        Status newStatus = new Status();
        assertNull(newStatus.getId());
        assertNull(newStatus.getName());
    }

    @Test
    void testModifyName() {
        status.setName("Disponible");
        assertEquals("Disponible", status.getName());

        status.setName("Operativa");
        assertEquals("Operativa", status.getName());
        assertNotEquals("Disponible", status.getName());
    }

    @Test
    void testModifyId() {
        status.setId(1L);
        assertEquals(1L, status.getId());

        status.setId(10L);
        assertEquals(10L, status.getId());
        assertNotEquals(1L, status.getId());
    }

    @Test
    void testCasePreservation() {
        status.setName("DISPONIBLE");
        assertEquals("DISPONIBLE", status.getName());
        assertNotEquals("disponible", status.getName());

        status.setName("disponible");
        assertEquals("disponible", status.getName());
        assertNotEquals("DISPONIBLE", status.getName());
    }

    @Test
    void testMultipleStatusObjects() {
        Status s1 = new Status();
        s1.setId(1L);
        s1.setName("Estado 1");

        Status s2 = new Status();
        s2.setId(2L);
        s2.setName("Estado 2");

        Status s3 = new Status();
        s3.setId(3L);
        s3.setName("Estado 3");

        assertNotEquals(s1.getId(), s2.getId());
        assertNotEquals(s2.getId(), s3.getId());
        assertNotEquals(s1.getName(), s3.getName());
    }

    @Test
    void testNameWithNumbers() {
        status.setName("Estado 123");
        assertEquals("Estado 123", status.getName());
    }
}
