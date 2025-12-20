package com.briamcarrasco.arriendomaquinaria.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para ReviewRequest.
 * Valida constraints de validación, getters, setters y comportamiento del DTO.
 */
class ReviewRequestTest {

    private ReviewRequest dto;
    private Validator validator;

    @BeforeEach
    void setUp() {
        dto = new ReviewRequest();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void setRating_shouldSetValue() {
        dto.setRating(5);

        assertEquals(5, dto.getRating());
    }

    @Test
    void setComment_shouldSetValue() {
        dto.setComment("Excelente maquinaria");

        assertEquals("Excelente maquinaria", dto.getComment());
    }

    @ParameterizedTest
    @CsvSource({
            "1, Mínimo",
            "3, Buen servicio",
            "4, Muy buena experiencia",
            "5, Máximo"
    })
    void validation_whenRatingIsValid_shouldPass(Integer rating, String comment) {
        dto.setRating(rating);
        dto.setComment(comment);

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1, 6, 10 })
    void validation_whenRatingIsOutOfRange_shouldFail(int rating) {
        dto.setRating(rating);
        dto.setComment("Comentario válido");

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("rating")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "   ", "\t", "\n" })
    void validation_whenCommentIsBlankOrNull_shouldFail(String comment) {
        dto.setRating(5);
        dto.setComment(comment);

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("comment")));
    }

    @Test
    void validation_withLongComment_shouldPass() {
        dto.setRating(5);
        dto.setComment("a".repeat(1000));

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_whenRatingIsNull_shouldNotFailMinMaxValidation() {
        dto.setRating(null);
        dto.setComment("Comentario válido");

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void setRating_withNegativeValue_shouldAccept() {
        dto.setRating(-5);

        assertEquals(-5, dto.getRating());
    }

    @Test
    void equals_withSameValues_shouldReturnTrue() {
        dto.setRating(5);
        dto.setComment("Excelente");

        ReviewRequest other = new ReviewRequest();
        other.setRating(5);
        other.setComment("Excelente");

        assertEquals(dto, other);
    }

    @Test
    void equals_withDifferentValues_shouldReturnFalse() {
        dto.setRating(5);
        dto.setComment("Excelente");

        ReviewRequest other = new ReviewRequest();
        other.setRating(3);
        other.setComment("Regular");

        assertNotEquals(dto, other);
    }

    @Test
    void hashCode_withSameValues_shouldReturnSameHashCode() {
        dto.setRating(4);
        dto.setComment("Bueno");

        ReviewRequest other = new ReviewRequest();
        other.setRating(4);
        other.setComment("Bueno");

        assertEquals(dto.hashCode(), other.hashCode());
    }

    @Test
    void toString_shouldContainFieldValues() {
        dto.setRating(5);
        dto.setComment("Excelente maquinaria");

        String result = dto.toString();

        assertTrue(result.contains("5"));
        assertTrue(result.contains("Excelente maquinaria"));
    }

    @Test
    void defaultConstructor_shouldCreateInstanceWithNullValues() {
        ReviewRequest newDto = new ReviewRequest();

        assertNull(newDto.getRating());
        assertNull(newDto.getComment());
    }

    @Test
    void validation_withSpecialCharactersInComment_shouldPass() {
        dto.setRating(5);
        dto.setComment("¡Excelente! @#$%^&*()");

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}