package com.briamcarrasco.arriendomaquinaria.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.briamcarrasco.arriendomaquinaria.model.User;
import com.briamcarrasco.arriendomaquinaria.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void createUser_whenUsernameExists_throws() {
        String username = "exists";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.createUser(username, "pass", "e@e.com"));
        assertEquals("El nombre de usuario ya existe.", ex.getMessage());

        verify(userRepository, times(1)).existsByUsername(username);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createUser_whenValid_savesAndReturnsUser() {
        String username = "newuser";
        String raw = "secret";
        String encoded = "ENCODED";
        String email = "u@example.com";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(raw)).thenReturn(encoded);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = service.createUser(username, raw, email);

        assertNotNull(saved);
        assertEquals(username, saved.getUsername());
        assertEquals(encoded, saved.getPassword());
        assertEquals(email, saved.getEmail());
        assertEquals(User.Role.USER, saved.getRole());

        verify(userRepository).existsByUsername(username);
        verify(passwordEncoder).encode(raw);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findByUsername_delegatesToRepository() {
        User u = new User();
        when(userRepository.findByUsername("bob")).thenReturn(u);

        User res = service.findByUsername("bob");

        assertSame(u, res);
        verify(userRepository).findByUsername("bob");
    }

    @Test
    void findById_whenPresent_returnsUser_elseNull() {
        User u = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertSame(u, service.findById(1L));
        assertNull(service.findById(2L));

        verify(userRepository, times(2)).findById(anyLong());
    }

    @Test
    void updateUser_whenNotFound_throws() {
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.updateUser(5L, "nx", "nx@ex.com"));
        assertEquals("Usuario no encontrado.", ex.getMessage());

        verify(userRepository).findById(5L);
    }

    @Test
    void updateUser_whenFound_updatesAndSaves() {
        User existing = new User();
        existing.setUsername("old");
        existing.setEmail("old@e.com");
        when(userRepository.findById(6L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User updated = service.updateUser(6L, "newuser", "new@e.com");

        assertSame(existing, updated);
        assertEquals("newuser", updated.getUsername());
        assertEquals("new@e.com", updated.getEmail());
        verify(userRepository).findById(6L);
        verify(userRepository).save(existing);
    }

    @Test
    void deleteUser_whenNotFound_throws() {
        when(userRepository.findById(7L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.deleteUser(7L));
        assertEquals("Usuario no encontrado.", ex.getMessage());

        verify(userRepository).findById(7L);
    }

    @Test
    void deleteUser_whenFound_deletesAndReturnsUser() {
        User u = new User();
        when(userRepository.findById(8L)).thenReturn(Optional.of(u));

        User res = service.deleteUser(8L);

        assertSame(u, res);
        verify(userRepository).findById(8L);
        verify(userRepository).delete(u);
    }

    @Test
    void getAllUsers_returnsRepositoryList() {
        User u1 = new User();
        User u2 = new User();
        List<User> list = Arrays.asList(u1, u2);
        when(userRepository.findAll()).thenReturn(list);

        List<User> res = service.getAllUsers();

        assertEquals(2, res.size());
        assertSame(list, res);
        verify(userRepository).findAll();
    }

        @Test
    void adminCreate_whenUsernameExists_throws() {
        String username = "admin";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.adminCreate(username, "pass", "admin@e.com", "ADMIN"));
        assertEquals("El nombre de usuario ya existe.", ex.getMessage());

        verify(userRepository, times(1)).existsByUsername(username);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void adminCreate_whenValid_savesAndReturnsUserWithRole() {
        String username = "adminuser";
        String raw = "adminpass";
        String encoded = "ENCODED_ADMIN";
        String email = "admin@example.com";
        String role = "ADMIN";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(raw)).thenReturn(encoded);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = service.adminCreate(username, raw, email, role);

        assertNotNull(saved);
        assertEquals(username, saved.getUsername());
        assertEquals(encoded, saved.getPassword());
        assertEquals(email, saved.getEmail());
        assertEquals(User.Role.ADMIN, saved.getRole());

        verify(userRepository).existsByUsername(username);
        verify(passwordEncoder).encode(raw);
        verify(userRepository).save(any(User.class));
    }
}