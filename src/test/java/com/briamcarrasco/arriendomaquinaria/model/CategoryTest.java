package com.briamcarrasco.arriendomaquinaria.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la entidad Category.
 */
class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    void defaultConstructor_shouldCreateInstance() {
        Category c = new Category();

        assertNotNull(c);
        assertNull(c.getId());
        assertNull(c.getName());
        assertNull(c.getDescription());
    }

    @Test
    void setId_shouldSetValue() {
        category.setId(1L);

        assertEquals(1L, category.getId());
    }

    @Test
    void setName_shouldSetValue() {
        category.setName("Excavadoras");

        assertEquals("Excavadoras", category.getName());
    }

    @Test
    void setDescription_shouldSetValue() {
        category.setDescription("Maquinaria de excavación");

        assertEquals("Maquinaria de excavación", category.getDescription());
    }

    @Test
    void setName_withNull_shouldAccept() {
        category.setName(null);

        assertNull(category.getName());
    }

    @Test
    void setDescription_withNull_shouldAccept() {
        category.setDescription(null);

        assertNull(category.getDescription());
    }

    @Test
    void setName_withEmptyString_shouldAccept() {
        category.setName("");

        assertEquals("", category.getName());
    }

    @Test
    void setDescription_withEmptyString_shouldAccept() {
        category.setDescription("");

        assertEquals("", category.getDescription());
    }

    @Test
    void equals_withSameValues_shouldReturnTrue() {
        category.setId(1L);
        category.setName("Excavadoras");
        category.setDescription("Descripción");

        Category other = new Category();
        other.setId(1L);
        other.setName("Excavadoras");
        other.setDescription("Descripción");

        assertEquals(category, other);
    }

    @Test
    void equals_withDifferentValues_shouldReturnFalse() {
        category.setId(1L);
        category.setName("Excavadoras");

        Category other = new Category();
        other.setId(2L);
        other.setName("Grúas");

        assertNotEquals(category, other);
    }

    @Test
    void hashCode_withSameValues_shouldReturnSameHashCode() {
        category.setId(1L);
        category.setName("Excavadoras");

        Category other = new Category();
        other.setId(1L);
        other.setName("Excavadoras");

        assertEquals(category.hashCode(), other.hashCode());
    }

    @Test
    void toString_shouldContainFieldValues() {
        category.setId(1L);
        category.setName("Excavadoras");
        category.setDescription("Maquinaria de excavación");

        String result = category.toString();

        assertTrue(result.contains("Excavadoras"));
    }

    @Test
    void setName_withLongString_shouldAccept() {
        String longName = "a".repeat(255);
        category.setName(longName);

        assertEquals(longName, category.getName());
    }

    @Test
    void setDescription_withLongString_shouldAccept() {
        String longDescription = "a".repeat(1000);
        category.setDescription(longDescription);

        assertEquals(longDescription, category.getDescription());
    }

    @Test
    void setName_withSpecialCharacters_shouldAccept() {
        category.setName("Maquinaria & Equipos #1");

        assertEquals("Maquinaria & Equipos #1", category.getName());
    }

    @Test
    void fullSetup_shouldMaintainAllValues() {
        category.setId(5L);
        category.setName("Grúas");
        category.setDescription("Equipos de elevación y movimiento");

        assertEquals(5L, category.getId());
        assertEquals("Grúas", category.getName());
        assertEquals("Equipos de elevación y movimiento", category.getDescription());
    }
}
