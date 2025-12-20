package com.briamcarrasco.arriendomaquinaria.dto;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para LoginRequestDto.
 * Valida getters, setters y comportamiento del DTO.
 */
class LoginRequestDtoTest {

    private LoginRequestDto dto;

    @BeforeEach
    void setUp() {
        dto = new LoginRequestDto();
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
    void setters_withNullValues_shouldAcceptNull() {
        dto.setUsername(null);
        dto.setPassword(null);

        assertNull(dto.getUsername());
        assertNull(dto.getPassword());
    }

    @Test
    void setters_withEmptyStrings_shouldAcceptEmpty() {
        dto.setUsername("");
        dto.setPassword("");

        assertEquals("", dto.getUsername());
        assertEquals("", dto.getPassword());
    }

    @Test
    void equals_withSameValues_shouldReturnTrue() {
        dto.setUsername("user");
        dto.setPassword("pass");

        LoginRequestDto other = new LoginRequestDto();
        other.setUsername("user");
        other.setPassword("pass");

        assertEquals(dto, other);
    }

    @Test
    void equals_withDifferentValues_shouldReturnFalse() {
        dto.setUsername("user1");
        dto.setPassword("pass1");

        LoginRequestDto other = new LoginRequestDto();
        other.setUsername("user2");
        other.setPassword("pass2");

        assertNotEquals(dto, other);
    }

    @Test
    void hashCode_withSameValues_shouldReturnSameHashCode() {
        dto.setUsername("user");
        dto.setPassword("pass");

        LoginRequestDto other = new LoginRequestDto();
        other.setUsername("user");
        other.setPassword("pass");

        assertEquals(dto.hashCode(), other.hashCode());
    }

    @Test
    void toString_shouldContainFieldValues() {
        dto.setUsername("testuser");
        dto.setPassword("password123");

        String result = dto.toString();

        assertTrue(result.contains("testuser"));
        assertTrue(result.contains("password123"));
    }

    @Test
    void setUsername_withSpecialCharacters_shouldAccept() {
        dto.setUsername("user@example.com");

        assertEquals("user@example.com", dto.getUsername());
    }

    @Test
    void setPassword_withSpecialCharacters_shouldAccept() {
        dto.setPassword("P@ssw0rd!#$");

        assertEquals("P@ssw0rd!#$", dto.getPassword());
    }

    @Test
    void setUsername_withLongString_shouldAccept() {
        String longUsername = "a".repeat(1000);
        dto.setUsername(longUsername);

        assertEquals(longUsername, dto.getUsername());
    }

    @Test
    void setPassword_withLongString_shouldAccept() {
        String longPassword = "p".repeat(1000);
        dto.setPassword(longPassword);

        assertEquals(longPassword, dto.getPassword());
    }

    @Test
    void defaultConstructor_shouldCreateInstanceWithNullValues() {
        LoginRequestDto newDto = new LoginRequestDto();

        assertNull(newDto.getUsername());
        assertNull(newDto.getPassword());
    }
}
