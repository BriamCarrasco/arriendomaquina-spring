package com.briamcarrasco.arriendomaquinaria.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la entidad Review.
 */
class ReviewTest {

    private Review review;
    private Machinery machinery;
    private User user;

    @BeforeEach
    void setUp() {
        review = new Review();

        machinery = new Machinery();
        machinery.setId(1L);
        machinery.setNameMachinery("Excavadora");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
    }

    @Test
    void defaultConstructor_shouldCreateInstance() {
        Review r = new Review();

        assertNotNull(r);
        assertNull(r.getId());
        assertNull(r.getMachinery());
        assertNull(r.getUser());
        assertNull(r.getRating());
        assertNull(r.getComment());
    }

    @Test
    void setId_shouldSetValue() {
        review.setId(10L);

        assertEquals(10L, review.getId());
    }

    @Test
    void setMachinery_shouldSetValue() {
        review.setMachinery(machinery);

        assertEquals(machinery, review.getMachinery());
        assertEquals("Excavadora", review.getMachinery().getNameMachinery());
    }

    @Test
    void setUser_shouldSetValue() {
        review.setUser(user);

        assertEquals(user, review.getUser());
        assertEquals("testuser", review.getUser().getUsername());
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 3, 5, 10, -1 })
    void setRating_shouldAcceptAnyValue(int rating) {
        review.setRating(rating);

        assertEquals(rating, review.getRating());
    }

    @ParameterizedTest
    @NullSource
    void setRating_withNull_shouldAccept(Integer rating) {
        review.setRating(rating);

        assertNull(review.getRating());
    }

    @Test
    void setComment_shouldSetValue() {
        review.setComment("Excelente maquinaria");

        assertEquals("Excelente maquinaria", review.getComment());
    }

    @Test
    void setCreatedAt_shouldSetValue() {
        LocalDateTime now = LocalDateTime.now();
        review.setCreatedAt(now);

        assertEquals(now, review.getCreatedAt());
    }

    @Test
    void setUpdatedAt_shouldSetValue() {
        LocalDateTime now = LocalDateTime.now();
        review.setUpdatedAt(now);

        assertEquals(now, review.getUpdatedAt());
    }

    @Test
    void setComment_withNull_shouldAccept() {
        review.setComment(null);

        assertNull(review.getComment());
    }

    @Test
    void setComment_withEmptyString_shouldAccept() {
        review.setComment("");

        assertEquals("", review.getComment());
    }

    @Test
    void setMachinery_withNull_shouldAccept() {
        review.setMachinery(null);

        assertNull(review.getMachinery());
    }

    @Test
    void setUser_withNull_shouldAccept() {
        review.setUser(null);

        assertNull(review.getUser());
    }

    @Test
    void equals_withSameValues_shouldReturnTrue() {
        review.setId(1L);
        review.setRating(5);
        review.setComment("Excelente");

        Review other = new Review();
        other.setId(1L);
        other.setRating(5);
        other.setComment("Excelente");

        assertEquals(review, other);
    }

    @Test
    void equals_withDifferentValues_shouldReturnFalse() {
        review.setId(1L);
        review.setRating(5);

        Review other = new Review();
        other.setId(2L);
        other.setRating(3);

        assertNotEquals(review, other);
    }

    @Test
    void hashCode_withSameValues_shouldReturnSameHashCode() {
        review.setId(1L);
        review.setRating(5);
        review.setComment("Test");

        Review other = new Review();
        other.setId(1L);
        other.setRating(5);
        other.setComment("Test");

        assertEquals(review.hashCode(), other.hashCode());
    }

    @Test
    void toString_shouldContainFieldValues() {
        review.setId(1L);
        review.setRating(5);
        review.setComment("Excelente");

        String result = review.toString();

        assertTrue(result.contains("5"));
        assertTrue(result.contains("Excelente"));
    }

    @Test
    void setComment_withLongString_shouldAccept() {
        String longComment = "a".repeat(1000);
        review.setComment(longComment);

        assertEquals(longComment, review.getComment());
    }

    @Test
    void setComment_withSpecialCharacters_shouldAccept() {
        review.setComment("¡Excelente! @#$%^&*()");

        assertEquals("¡Excelente! @#$%^&*()", review.getComment());
    }

    @Test
    void fullSetup_shouldMaintainAllValues() {
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime updated = LocalDateTime.now();

        review.setId(1L);
        review.setMachinery(machinery);
        review.setUser(user);
        review.setRating(4);
        review.setComment("Muy buena experiencia");
        review.setCreatedAt(created);
        review.setUpdatedAt(updated);

        assertEquals(1L, review.getId());
        assertEquals(machinery, review.getMachinery());
        assertEquals(user, review.getUser());
        assertEquals(4, review.getRating());
        assertEquals("Muy buena experiencia", review.getComment());
        assertEquals(created, review.getCreatedAt());
        assertEquals(updated, review.getUpdatedAt());
    }
}