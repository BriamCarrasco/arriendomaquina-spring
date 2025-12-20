package com.briamcarrasco.arriendomaquinaria.repository;

import com.briamcarrasco.arriendomaquinaria.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para UserRepository.
 */
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        user1 = new User();
        user1.setUsername("testuser");
        user1.setPassword("password123");
        user1.setEmail("test@example.com");
        user1.setRole(User.Role.USER);

        user2 = new User();
        user2.setUsername("adminuser");
        user2.setPassword("admin123");
        user2.setEmail("admin@example.com");
        user2.setRole(User.Role.ADMIN);
    }

    @Test
    void save_shouldPersistUser() {
        User saved = userRepository.save(user1);

        assertNotNull(saved.getId());
        assertEquals("testuser", saved.getUsername());
        assertEquals("test@example.com", saved.getEmail());
    }

    @Test
    void findById_whenExists_shouldReturnUser() {
        User saved = entityManager.persistAndFlush(user1);

        Optional<User> found = userRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
    }

    @Test
    void findById_whenNotExists_shouldReturnEmpty() {
        Optional<User> found = userRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void findByUsername_whenExists_shouldReturnUser() {
        entityManager.persistAndFlush(user1);

        User found = userRepository.findByUsername("testuser");

        assertNotNull(found);
        assertEquals("testuser", found.getUsername());
        assertEquals("test@example.com", found.getEmail());
    }

    @Test
    void findByUsername_whenNotExists_shouldReturnNull() {
        User found = userRepository.findByUsername("noexiste");

        assertNull(found);
    }

    @Test
    void existsByUsername_whenExists_shouldReturnTrue() {
        entityManager.persistAndFlush(user1);

        boolean exists = userRepository.existsByUsername("testuser");

        assertTrue(exists);
    }

    @Test
    void existsByUsername_whenNotExists_shouldReturnFalse() {
        boolean exists = userRepository.existsByUsername("noexiste");

        assertFalse(exists);
    }

    @Test
    void delete_shouldRemoveUser() {
        User saved = entityManager.persistAndFlush(user1);
        Long id = saved.getId();

        userRepository.delete(saved);
        entityManager.flush();

        Optional<User> found = userRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void update_shouldModifyUser() {
        User saved = entityManager.persistAndFlush(user1);

        saved.setEmail("newemail@example.com");
        saved.setRole(User.Role.ADMIN);
        User updated = userRepository.save(saved);
        entityManager.flush();

        User found = userRepository.findById(updated.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("newemail@example.com", found.getEmail());
        assertEquals(User.Role.ADMIN, found.getRole());
    }

    @Test
    void count_shouldReturnCorrectCount() {
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        long count = userRepository.count();

        assertEquals(2, count);
    }

    @Test
    void findByUsername_shouldBeCaseSensitive() {
        entityManager.persistAndFlush(user1);

        User found = userRepository.findByUsername("TESTUSER");

        assertNull(found);
    }

    @Test
    void existsByUsername_shouldBeCaseSensitive() {
        entityManager.persistAndFlush(user1);

        boolean exists = userRepository.existsByUsername("TESTUSER");

        assertFalse(exists);
    }
}
