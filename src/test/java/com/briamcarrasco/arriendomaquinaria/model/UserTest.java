package com.briamcarrasco.arriendomaquinaria.model;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

class UserTest {

    @Test
    void gettersAndSetters_workAsExpected() {
        User u = new User();

        u.setUsername("usuario");
        u.setPassword("secreto");
        u.setEmail("u@ejemplo.com");
        u.setRole(User.Role.USER);

        assertEquals("usuario", u.getUsername());
        assertEquals("secreto", u.getPassword());
        assertEquals("u@ejemplo.com", u.getEmail());
        assertEquals(User.Role.USER, u.getRole());
    }

    @Test
    void roleEnum_containsUserConstant() {
        assertNotNull(User.Role.USER);
    }

    @Test
    void defaultNewUser_hasNullFieldsUntilSet() {
        User u = new User();
        assertNull(u.getUsername());
        assertNull(u.getPassword());
        assertNull(u.getEmail());
        try {
            u.getRole();
        } catch (Exception ex) {
            fail("getRole lanzó una excepción: " + ex.getMessage());
        }
    }

    @Test
    void getAuthorities_withUserRole_returnsRoleUser() {
        User u = new User();
        u.setRole(User.Role.USER);

        Collection<? extends GrantedAuthority> authorities = u.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void getAuthorities_withAdminRole_returnsRoleAdmin() {
        User u = new User();
        u.setRole(User.Role.ADMIN);

        Collection<? extends GrantedAuthority> authorities = u.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void isAccountNonExpired_returnsTrue() {
        User u = new User();
        assertTrue(u.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked_returnsTrue() {
        User u = new User();
        assertTrue(u.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired_returnsTrue() {
        User u = new User();
        assertTrue(u.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_returnsTrue() {
        User u = new User();
        assertTrue(u.isEnabled());
    }

    @Test
    void roleEnum_containsAdminConstant() {
        assertNotNull(User.Role.ADMIN);
    }

    @Test
    void setUsername_withNullValue_shouldAcceptNull() {
        User u = new User();
        u.setUsername(null);
        assertNull(u.getUsername());
    }

    @Test
    void setEmail_withEmptyString_shouldAcceptEmpty() {
        User u = new User();
        u.setEmail("");
        assertEquals("", u.getEmail());
    }

    @Test
    void getAuthorities_withNullRole_shouldThrowException() {
        User u = new User();
        u.setRole(null);
        Exception ex = assertThrows(NullPointerException.class, () -> u.getAuthorities());
        assertNotNull(ex);
    }

    @Test
    void equals_withSameData_shouldReturnTrue() {
        User u1 = new User();
        u1.setId(1L);
        u1.setUsername("test");
        u1.setEmail("test@test.com");

        User u2 = new User();
        u2.setId(1L);
        u2.setUsername("test");
        u2.setEmail("test@test.com");

        assertEquals(u1, u2);
    }

    @Test
    void hashCode_withSameData_shouldBeSame() {
        User u1 = new User();
        u1.setId(1L);
        u1.setUsername("test");

        User u2 = new User();
        u2.setId(1L);
        u2.setUsername("test");

        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void roleEnum_valueOf_shouldWorkCorrectly() {
        assertEquals(User.Role.USER, User.Role.valueOf("USER"));
        assertEquals(User.Role.ADMIN, User.Role.valueOf("ADMIN"));
    }
}