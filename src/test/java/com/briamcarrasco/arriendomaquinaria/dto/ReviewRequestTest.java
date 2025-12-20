package com.briamcarrasco.arriendomaquinaria.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    void validation_whenRatingIsValid_shouldPass() {
        dto.setRating(3);
        dto.setComment("Buen servicio");

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_whenRatingIsBelowMinimum_shouldFail() {
        dto.setRating(0);
        dto.setComment("Mal servicio");

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("rating")));
    }

    @Test
    void validation_whenRatingIsAboveMaximum_shouldFail() {
        dto.setRating(6);
        dto.setComment("Excelente");

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("rating")));
    }

    @Test
    void validation_whenRatingIsMinimumValue_shouldPass() {
        dto.setRating(1);
        dto.setComment("Mínimo");

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_whenRatingIsMaximumValue_shouldPass() {
        dto.setRating(5);
        dto.setComment("Máximo");

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_whenCommentIsBlank_shouldFail() {
        dto.setRating(5);
        dto.setComment("");

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("comment")));
    }

    @Test
    void validation_whenCommentIsNull_shouldFail() {
        dto.setRating(5);
        dto.setComment(null);

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("comment")));
    }

    @Test
    void validation_whenCommentIsWhitespaceOnly_shouldFail() {
        dto.setRating(5);
        dto.setComment("   ");

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("comment")));
    }

    @Test
    void validation_whenAllFieldsAreValid_shouldPass() {
        dto.setRating(4);
        dto.setComment("Muy buena experiencia");

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
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
        // @Min y @Max solo se aplican si el valor no es null
        dto.setRating(null);
        dto.setComment("Comentario válido");

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(dto);

        // Solo debería haber un error por rating null si tiene @NotNull
        // Como no lo tiene en el código original, debería pasar
        assertTrue(violations.isEmpty());
    }

    @Test
    void setRating_withNegativeValue_shouldAccept() {
        // El setter acepta cualquier valor, las validaciones se hacen aparte
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
