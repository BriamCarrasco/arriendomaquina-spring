package com.briamcarrasco.arriendomaquinaria.repository;

import com.briamcarrasco.arriendomaquinaria.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para CategoryRepository.
 * Utiliza @DataJpaTest para probar operaciones de base de datos con una BD en
 * memoria.
 */
@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        // Limpiar datos previos
        categoryRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        // Crear datos de prueba
        category1 = new Category();
        category1.setName("Excavadoras");
        category1.setDescription("Maquinaria para excavación");

        category2 = new Category();
        category2.setName("Grúas");
        category2.setDescription("Maquinaria para elevación");
    }

    @Test
    void save_shouldPersistCategory() {
        Category saved = categoryRepository.save(category1);

        assertNotNull(saved.getId());
        assertEquals("Excavadoras", saved.getName());
        assertEquals("Maquinaria para excavación", saved.getDescription());
    }

    @Test
    void findById_whenExists_shouldReturnCategory() {
        Category saved = entityManager.persistAndFlush(category1);

        Optional<Category> found = categoryRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Excavadoras", found.get().getName());
    }

    @Test
    void findById_whenNotExists_shouldReturnEmpty() {
        Optional<Category> found = categoryRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void findAll_shouldReturnAllCategories() {
        entityManager.persist(category1);
        entityManager.persist(category2);
        entityManager.flush();

        List<Category> categories = categoryRepository.findAll();

        assertEquals(2, categories.size());
    }

    @Test
    void findAll_whenEmpty_shouldReturnEmptyList() {
        List<Category> categories = categoryRepository.findAll();

        assertTrue(categories.isEmpty());
    }

    @Test
    void findByName_whenExists_shouldReturnCategory() {
        entityManager.persistAndFlush(category1);

        Category found = categoryRepository.findByName("Excavadoras");

        assertNotNull(found);
        assertEquals("Excavadoras", found.getName());
    }

    @Test
    void findByName_whenNotExists_shouldReturnNull() {
        Category found = categoryRepository.findByName("NoExiste");

        assertNull(found);
    }

    @Test
    void existsByName_whenExists_shouldReturnTrue() {
        entityManager.persistAndFlush(category1);

        boolean exists = categoryRepository.existsByName("Excavadoras");

        assertTrue(exists);
    }

    @Test
    void existsByName_whenNotExists_shouldReturnFalse() {
        boolean exists = categoryRepository.existsByName("NoExiste");

        assertFalse(exists);
    }

    @Test
    void delete_shouldRemoveCategory() {
        Category saved = entityManager.persistAndFlush(category1);
        Long id = saved.getId();

        categoryRepository.delete(saved);
        entityManager.flush();

        Optional<Category> found = categoryRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void deleteById_shouldRemoveCategory() {
        Category saved = entityManager.persistAndFlush(category1);
        Long id = saved.getId();

        categoryRepository.deleteById(id);
        entityManager.flush();

        Optional<Category> found = categoryRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void update_shouldModifyCategory() {
        Category saved = entityManager.persistAndFlush(category1);

        saved.setName("Excavadoras Hidráulicas");
        saved.setDescription("Descripción actualizada");
        Category updated = categoryRepository.save(saved);
        entityManager.flush();

        Category found = categoryRepository.findById(updated.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Excavadoras Hidráulicas", found.getName());
        assertEquals("Descripción actualizada", found.getDescription());
    }

    @Test
    void count_shouldReturnCorrectCount() {
        entityManager.persist(category1);
        entityManager.persist(category2);
        entityManager.flush();

        long count = categoryRepository.count();

        assertEquals(2, count);
    }

    @Test
    void findByName_shouldBeCaseSensitive() {
        entityManager.persistAndFlush(category1);

        Category found = categoryRepository.findByName("excavadoras");

        assertNull(found);
    }
}
