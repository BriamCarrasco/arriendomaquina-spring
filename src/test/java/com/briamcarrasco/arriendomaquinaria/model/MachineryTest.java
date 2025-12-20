package com.briamcarrasco.arriendomaquinaria.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la entidad Machinery.
 * Valida constructores, getters, setters y relaciones.
 */
class MachineryTest {

    private Machinery machinery;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Excavadoras");
        category.setDescription("Maquinaria de excavación");

        machinery = new Machinery();
    }

    @Test
    void noArgsConstructor_shouldCreateInstance() {
        Machinery m = new Machinery();

        assertNotNull(m);
        assertNull(m.getId());
        assertNull(m.getNameMachinery());
        assertNull(m.getStatus());
        assertNull(m.getPricePerDay());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        MachineryInfo info = new MachineryInfo();
        List<Review> reviews = new ArrayList<>();
        List<MachineryMedia> media = new ArrayList<>();

        Machinery m = new Machinery(
                1L,
                "Excavadora CAT",
                "Disponible",
                new BigDecimal("5000"),
                "http://image.url",
                category,
                info,
                reviews,
                media);

        assertEquals(1L, m.getId());
        assertEquals("Excavadora CAT", m.getNameMachinery());
        assertEquals("Disponible", m.getStatus());
        assertEquals(new BigDecimal("5000"), m.getPricePerDay());
        assertEquals("http://image.url", m.getImageUrl());
        assertEquals(category, m.getCategory());
        assertEquals(info, m.getMachineryInfo());
        assertEquals(reviews, m.getReviews());
        assertEquals(media, m.getMedia());
    }

    @Test
    void setId_shouldSetValue() {
        machinery.setId(10L);

        assertEquals(10L, machinery.getId());
    }

    @Test
    void setNameMachinery_shouldSetValue() {
        machinery.setNameMachinery("Excavadora CAT 320");

        assertEquals("Excavadora CAT 320", machinery.getNameMachinery());
    }

    @Test
    void setStatus_shouldSetValue() {
        machinery.setStatus("Disponible");

        assertEquals("Disponible", machinery.getStatus());
    }

    @Test
    void setPricePerDay_shouldSetValue() {
        BigDecimal price = new BigDecimal("5000.50");
        machinery.setPricePerDay(price);

        assertEquals(price, machinery.getPricePerDay());
    }

    @Test
    void setImageUrl_shouldSetValue() {
        machinery.setImageUrl("http://example.com/image.jpg");

        assertEquals("http://example.com/image.jpg", machinery.getImageUrl());
    }

    @Test
    void setCategory_shouldSetValue() {
        machinery.setCategory(category);

        assertEquals(category, machinery.getCategory());
        assertEquals("Excavadoras", machinery.getCategory().getName());
    }

    @Test
    void setMachineryInfo_shouldSetValue() {
        MachineryInfo info = new MachineryInfo();
        info.setId(1L);
        info.setDescription("Descripción detallada");

        machinery.setMachineryInfo(info);

        assertEquals(info, machinery.getMachineryInfo());
        assertEquals(1L, machinery.getMachineryInfo().getId());
    }

    @Test
    void setReviews_shouldSetValue() {
        Review review1 = new Review();
        review1.setId(1L);
        Review review2 = new Review();
        review2.setId(2L);
        List<Review> reviews = List.of(review1, review2);

        machinery.setReviews(reviews);

        assertEquals(2, machinery.getReviews().size());
        assertTrue(machinery.getReviews().contains(review1));
        assertTrue(machinery.getReviews().contains(review2));
    }

    @Test
    void setMedia_shouldSetValue() {
        MachineryMedia media1 = new MachineryMedia();
        media1.setId(1L);
        MachineryMedia media2 = new MachineryMedia();
        media2.setId(2L);
        List<MachineryMedia> mediaList = List.of(media1, media2);

        machinery.setMedia(mediaList);

        assertEquals(2, machinery.getMedia().size());
        assertTrue(machinery.getMedia().contains(media1));
        assertTrue(machinery.getMedia().contains(media2));
    }

    @Test
    void setNameMachinery_withNull_shouldAccept() {
        machinery.setNameMachinery(null);

        assertNull(machinery.getNameMachinery());
    }

    @Test
    void setStatus_withNull_shouldAccept() {
        machinery.setStatus(null);

        assertNull(machinery.getStatus());
    }

    @Test
    void setPricePerDay_withNull_shouldAccept() {
        machinery.setPricePerDay(null);

        assertNull(machinery.getPricePerDay());
    }

    @Test
    void setCategory_withNull_shouldAccept() {
        machinery.setCategory(null);

        assertNull(machinery.getCategory());
    }

    @Test
    void toString_shouldContainFieldValues() {
        machinery.setId(1L);
        machinery.setNameMachinery("Excavadora");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("5000"));

        String result = machinery.toString();

        assertTrue(result.contains("Excavadora"));
        assertTrue(result.contains("Disponible"));
    }

    @Test
    void setPricePerDay_withZero_shouldAccept() {
        machinery.setPricePerDay(BigDecimal.ZERO);

        assertEquals(BigDecimal.ZERO, machinery.getPricePerDay());
    }

    @Test
    void setPricePerDay_withNegativeValue_shouldAccept() {
        // El setter acepta cualquier valor, las validaciones de negocio se hacen en
        // otro nivel
        machinery.setPricePerDay(new BigDecimal("-100"));

        assertEquals(new BigDecimal("-100"), machinery.getPricePerDay());
    }

    @Test
    void setNameMachinery_withEmptyString_shouldAccept() {
        machinery.setNameMachinery("");

        assertEquals("", machinery.getNameMachinery());
    }

    @Test
    void setStatus_withEmptyString_shouldAccept() {
        machinery.setStatus("");

        assertEquals("", machinery.getStatus());
    }

    @Test
    void setImageUrl_withNull_shouldAccept() {
        machinery.setImageUrl(null);

        assertNull(machinery.getImageUrl());
    }

    @Test
    void setMachineryInfo_withNull_shouldAccept() {
        machinery.setMachineryInfo(null);

        assertNull(machinery.getMachineryInfo());
    }

    @Test
    void setReviews_withEmptyList_shouldAccept() {
        machinery.setReviews(new ArrayList<>());

        assertNotNull(machinery.getReviews());
        assertTrue(machinery.getReviews().isEmpty());
    }

    @Test
    void setMedia_withEmptyList_shouldAccept() {
        machinery.setMedia(new ArrayList<>());

        assertNotNull(machinery.getMedia());
        assertTrue(machinery.getMedia().isEmpty());
    }

    @Test
    void setReviews_withNull_shouldAccept() {
        machinery.setReviews(null);

        assertNull(machinery.getReviews());
    }

    @Test
    void setMedia_withNull_shouldAccept() {
        machinery.setMedia(null);

        assertNull(machinery.getMedia());
    }

    @Test
    void setPricePerDay_withLargeValue_shouldAccept() {
        BigDecimal largePrice = new BigDecimal("9999999999.99");
        machinery.setPricePerDay(largePrice);

        assertEquals(largePrice, machinery.getPricePerDay());
    }

    @Test
    void setNameMachinery_withSpecialCharacters_shouldAccept() {
        machinery.setNameMachinery("Excavadora #1 - CAT 320D");

        assertEquals("Excavadora #1 - CAT 320D", machinery.getNameMachinery());
    }

    @Test
    void complexScenario_fullMachinerySetup() {
        machinery.setId(1L);
        machinery.setNameMachinery("Excavadora CAT 320");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("5000"));
        machinery.setImageUrl("http://example.com/image.jpg");
        machinery.setCategory(category);

        MachineryInfo info = new MachineryInfo();
        info.setDescription("Máquina en excelente estado");
        machinery.setMachineryInfo(info);

        assertNotNull(machinery.getId());
        assertNotNull(machinery.getNameMachinery());
        assertNotNull(machinery.getStatus());
        assertNotNull(machinery.getPricePerDay());
        assertNotNull(machinery.getCategory());
        assertNotNull(machinery.getMachineryInfo());
    }
}
