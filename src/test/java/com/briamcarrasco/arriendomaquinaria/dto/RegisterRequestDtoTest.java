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
 * Tests para RegisterRequestDto.
 * Valida constraints de validación, getters, setters y comportamiento del DTO.
 */
class RegisterRequestDtoTest {

    private RegisterRequestDto dto;
    private Validator validator;

    @BeforeEach
    void setUp() {
        dto = new RegisterRequestDto();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void setUsername_shouldSetValue() {
        dto.setUsername("testuser");

        assertEquals("testuser", dto.getUsername());
    }

    @Test
    void setPassword_shouldSetValue() {
        dto.setPassword("password123");

        assertEquals("password123", dto.getPassword());
    }

    @Test
    void setEmail_shouldSetValue() {
        dto.setEmail("test@example.com");

        assertEquals("test@example.com", dto.getEmail());
    }

    @Test
    void validation_whenUsernameIsBlank_shouldFail() {
        dto.setUsername("");
        dto.setPassword("password");
        dto.setEmail("test@example.com");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validation_whenUsernameIsNull_shouldFail() {
        dto.setUsername(null);
        dto.setPassword("password");
        dto.setEmail("test@example.com");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validation_whenPasswordIsBlank_shouldFail() {
        dto.setUsername("testuser");
        dto.setPassword("");
        dto.setEmail("test@example.com");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void validation_whenPasswordIsNull_shouldFail() {
        dto.setUsername("testuser");
        dto.setPassword(null);
        dto.setEmail("test@example.com");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void validation_whenEmailIsInvalid_shouldFail() {
        dto.setUsername("testuser");
        dto.setPassword("password");
        dto.setEmail("invalid-email");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void validation_whenEmailIsBlank_shouldFail() {
        dto.setUsername("testuser");
        dto.setPassword("password");
        dto.setEmail("");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void validation_whenEmailIsNull_shouldFail() {
        dto.setUsername("testuser");
        dto.setPassword("password");
        dto.setEmail(null);

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void validation_whenAllFieldsAreValid_shouldPass() {
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_withValidEmailFormats_shouldPass() {
        dto.setUsername("testuser");
        dto.setPassword("password");

        String[] validEmails = {
                "user@example.com",
                "user.name@example.com",
                "user+tag@example.co.uk",
                "user123@test-domain.com"
        };

        for (String email : validEmails) {
            dto.setEmail(email);
            Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);
            assertTrue(violations.isEmpty(), "Email debería ser válido: " + email);
        }
    }

    @Test
    void validation_withInvalidEmailFormats_shouldFail() {
        dto.setUsername("testuser");
        dto.setPassword("password");

        String[] invalidEmails = {
                "invalid",
                "@example.com",
                "user@",
                "user..name@example.com",
                "user @example.com"
        };

        for (String email : invalidEmails) {
            dto.setEmail(email);
            Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);
            assertFalse(violations.isEmpty(), "Email debería ser inválido: " + email);
        }
    }

    @Test
    void equals_withSameValues_shouldReturnTrue() {
        dto.setUsername("user");
        dto.setPassword("pass");
        dto.setEmail("test@example.com");

        RegisterRequestDto other = new RegisterRequestDto();
        other.setUsername("user");
        other.setPassword("pass");
        other.setEmail("test@example.com");

        assertEquals(dto, other);
    }

    @Test
    void equals_withDifferentValues_shouldReturnFalse() {
        dto.setUsername("user1");
        dto.setPassword("pass1");
        dto.setEmail("test1@example.com");

        RegisterRequestDto other = new RegisterRequestDto();
        other.setUsername("user2");
        other.setPassword("pass2");
        other.setEmail("test2@example.com");

        assertNotEquals(dto, other);
    }

    @Test
    void hashCode_withSameValues_shouldReturnSameHashCode() {
        dto.setUsername("user");
        dto.setPassword("pass");
        dto.setEmail("test@example.com");

        RegisterRequestDto other = new RegisterRequestDto();
        other.setUsername("user");
        other.setPassword("pass");
        other.setEmail("test@example.com");

        assertEquals(dto.hashCode(), other.hashCode());
    }

    @Test
    void toString_shouldContainFieldValues() {
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");

        String result = dto.toString();

        assertTrue(result.contains("testuser"));
        assertTrue(result.contains("password123"));
        assertTrue(result.contains("test@example.com"));
    }

    @Test
    void defaultConstructor_shouldCreateInstanceWithNullValues() {
        RegisterRequestDto newDto = new RegisterRequestDto();

        assertNull(newDto.getUsername());
        assertNull(newDto.getPassword());
        assertNull(newDto.getEmail());
    }
}
