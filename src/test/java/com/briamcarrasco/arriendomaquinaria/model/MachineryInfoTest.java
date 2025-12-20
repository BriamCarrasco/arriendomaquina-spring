package com.briamcarrasco.arriendomaquinaria.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class MachineryInfoTest {

    private MachineryInfo machineryInfo;
    private Machinery machinery;

    @BeforeEach
    void setUp() {
        machineryInfo = new MachineryInfo();
        machinery = new Machinery();
    }

    @Test
    void testGetSetId() {
        Long id = 1L;
        machineryInfo.setId(id);
        assertEquals(id, machineryInfo.getId());
    }

    @Test
    void testGetSetDescription() {
        String description = "Excavadora Caterpillar 320D con brazo extensible";
        machineryInfo.setDescription(description);
        assertEquals(description, machineryInfo.getDescription());
    }

    @Test
    void testGetSetMachinery() {
        machinery.setId(1L);
        machinery.setNameMachinery("Excavadora");
        machineryInfo.setMachinery(machinery);

        assertNotNull(machineryInfo.getMachinery());
        assertEquals(machinery, machineryInfo.getMachinery());
        assertEquals("Excavadora", machineryInfo.getMachinery().getNameMachinery());
    }

    @Test
    void testDescriptionCanBeNull() {
        machineryInfo.setDescription(null);
        assertNull(machineryInfo.getDescription());
    }

    @Test
    void testMachineryCanBeNull() {
        machineryInfo.setMachinery(null);
        assertNull(machineryInfo.getMachinery());
    }

    @Test
    void testEqualsAndHashCode() {
        MachineryInfo info1 = new MachineryInfo();
        info1.setId(1L);
        info1.setDescription("Description 1");

        MachineryInfo info2 = new MachineryInfo();
        info2.setId(1L);
        info2.setDescription("Description 1");

        assertEquals(info1, info2);
        assertEquals(info1.hashCode(), info2.hashCode());
    }

    @Test
    void testNotEquals() {
        MachineryInfo info1 = new MachineryInfo();
        info1.setId(1L);
        info1.setDescription("Description 1");

        MachineryInfo info2 = new MachineryInfo();
        info2.setId(2L);
        info2.setDescription("Description 2");

        assertNotEquals(info1, info2);
    }

    @Test
    void testToString() {
        machineryInfo.setId(1L);
        machineryInfo.setDescription("Test description");

        String toString = machineryInfo.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("MachineryInfo"));
    }

    @Test
    void testCreateWithAllFields() {
        MachineryInfo info = new MachineryInfo();
        info.setId(10L);
        info.setDescription("Maquinaria de construcción pesada");

        Machinery testMachinery = new Machinery();
        testMachinery.setId(5L);
        testMachinery.setNameMachinery("Bulldozer");
        info.setMachinery(testMachinery);

        assertEquals(10L, info.getId());
        assertEquals("Maquinaria de construcción pesada", info.getDescription());
        assertNotNull(info.getMachinery());
        assertEquals(5L, info.getMachinery().getId());
    }

    @Test
    void testEmptyDescription() {
        machineryInfo.setDescription("");
        assertEquals("", machineryInfo.getDescription());
        assertTrue(machineryInfo.getDescription().isEmpty());
    }

    @Test
    void testLongDescription() {
        String longDescription = "A".repeat(500);
        machineryInfo.setDescription(longDescription);
        assertEquals(longDescription, machineryInfo.getDescription());
        assertEquals(500, machineryInfo.getDescription().length());
    }

    @Test
    void testMachineryRelationship() {
        Machinery m = new Machinery();
        m.setId(100L);
        m.setNameMachinery("Grúa Torre");
        m.setStatus("Disponible");

        machineryInfo.setMachinery(m);

        assertNotNull(machineryInfo.getMachinery());
        assertEquals(100L, machineryInfo.getMachinery().getId());
        assertEquals("Grúa Torre", machineryInfo.getMachinery().getNameMachinery());
        assertEquals("Disponible", machineryInfo.getMachinery().getStatus());
    }

    @Test
    void testIdInitiallyNull() {
        MachineryInfo newInfo = new MachineryInfo();
        assertNull(newInfo.getId());
    }

    @Test
    void testDescriptionInitiallyNull() {
        MachineryInfo newInfo = new MachineryInfo();
        assertNull(newInfo.getDescription());
    }

    @Test
    void testMachineryInitiallyNull() {
        MachineryInfo newInfo = new MachineryInfo();
        assertNull(newInfo.getMachinery());
    }
}
