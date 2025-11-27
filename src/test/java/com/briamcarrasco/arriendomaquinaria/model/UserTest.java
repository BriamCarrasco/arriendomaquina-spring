package com.briamcarrasco.arriendomaquinaria.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

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
}