package com.briamcarrasco.arriendomaquinaria.repository;

import com.briamcarrasco.arriendomaquinaria.model.Category;
import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para MachineryRepository.
 */
@DataJpaTest
class MachineryRepositoryTest {

    @Autowired
    private MachineryRepository machineryRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Category category;
    private Machinery machinery1;
    private Machinery machinery2;

    @BeforeEach
    void setUp() {
        machineryRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        // Crear categoría
        category = new Category();
        category.setName("Excavadoras");
        category.setDescription("Maquinaria para excavación");
        category = entityManager.persistAndFlush(category);

        // Crear maquinarias
        machinery1 = new Machinery();
        machinery1.setNameMachinery("Excavadora CAT 320");
        machinery1.setStatus("Disponible");
        machinery1.setPricePerDay(new BigDecimal("5000"));
        machinery1.setCategory(category);

        machinery2 = new Machinery();
        machinery2.setNameMachinery("Excavadora Komatsu");
        machinery2.setStatus("Arrendada");
        machinery2.setPricePerDay(new BigDecimal("4500"));
        machinery2.setCategory(category);
    }

    @Test
    void save_shouldPersistMachinery() {
        Machinery saved = machineryRepository.save(machinery1);

        assertNotNull(saved.getId());
        assertEquals("Excavadora CAT 320", saved.getNameMachinery());
        assertEquals("Disponible", saved.getStatus());
        assertEquals(new BigDecimal("5000"), saved.getPricePerDay());
    }

    @Test
    void findById_whenExists_shouldReturnMachinery() {
        Machinery saved = entityManager.persistAndFlush(machinery1);

        Optional<Machinery> found = machineryRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Excavadora CAT 320", found.get().getNameMachinery());
    }

    @Test
    void findById_whenNotExists_shouldReturnEmpty() {
        Optional<Machinery> found = machineryRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void findAll_shouldReturnAllMachinery() {
        entityManager.persist(machinery1);
        entityManager.persist(machinery2);
        entityManager.flush();

        List<Machinery> machineries = machineryRepository.findAll();

        assertEquals(2, machineries.size());
    }

    @Test
    void existsByNameMachinery_whenExists_shouldReturnTrue() {
        entityManager.persistAndFlush(machinery1);

        boolean exists = machineryRepository.existsByNameMachinery("Excavadora CAT 320");

        assertTrue(exists);
    }

    @Test
    void existsByNameMachinery_whenNotExists_shouldReturnFalse() {
        boolean exists = machineryRepository.existsByNameMachinery("NoExiste");

        assertFalse(exists);
    }

    @Test
    void findByNameMachinery_whenExists_shouldReturnMachinery() {
        entityManager.persistAndFlush(machinery1);

        Machinery found = machineryRepository.findByNameMachinery("Excavadora CAT 320");

        assertNotNull(found);
        assertEquals("Excavadora CAT 320", found.getNameMachinery());
    }

    @Test
    void findByNameMachinery_whenNotExists_shouldReturnNull() {
        Machinery found = machineryRepository.findByNameMachinery("NoExiste");

        assertNull(found);
    }

    @Test
    void findByNameMachineryContainingIgnoreCase_shouldReturnMatchingMachinery() {
        entityManager.persist(machinery1);
        entityManager.persist(machinery2);
        entityManager.flush();

        List<Machinery> found = machineryRepository.findByNameMachineryContainingIgnoreCase("excavadora");

        assertEquals(2, found.size());
    }

    @Test
    void findByNameMachineryContainingIgnoreCase_shouldBeCaseInsensitive() {
        entityManager.persistAndFlush(machinery1);

        List<Machinery> found = machineryRepository.findByNameMachineryContainingIgnoreCase("CAT");

        assertEquals(1, found.size());
        assertEquals("Excavadora CAT 320", found.get(0).getNameMachinery());
    }

    @Test
    void findByNameMachineryContainingIgnoreCase_whenNoMatch_shouldReturnEmpty() {
        entityManager.persistAndFlush(machinery1);

        List<Machinery> found = machineryRepository.findByNameMachineryContainingIgnoreCase("Grúa");

        assertTrue(found.isEmpty());
    }

    @Test
    void findByCategory_NameIgnoreCase_shouldReturnMachineryByCategory() {
        entityManager.persist(machinery1);
        entityManager.persist(machinery2);
        entityManager.flush();

        List<Machinery> found = machineryRepository.findByCategory_NameIgnoreCase("Excavadoras");

        assertEquals(2, found.size());
    }

    @Test
    void findByCategory_NameIgnoreCase_shouldBeCaseInsensitive() {
        entityManager.persist(machinery1);
        entityManager.persist(machinery2);
        entityManager.flush();

        List<Machinery> found = machineryRepository.findByCategory_NameIgnoreCase("excavadoras");

        assertEquals(2, found.size());
    }

    @Test
    void findByCategory_NameIgnoreCase_whenNoMatch_shouldReturnEmpty() {
        entityManager.persistAndFlush(machinery1);

        List<Machinery> found = machineryRepository.findByCategory_NameIgnoreCase("Grúas");

        assertTrue(found.isEmpty());
    }

    @Test
    void delete_shouldRemoveMachinery() {
        Machinery saved = entityManager.persistAndFlush(machinery1);
        Long id = saved.getId();

        machineryRepository.delete(saved);
        entityManager.flush();

        Optional<Machinery> found = machineryRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void update_shouldModifyMachinery() {
        Machinery saved = entityManager.persistAndFlush(machinery1);

        saved.setStatus("Mantenimiento");
        saved.setPricePerDay(new BigDecimal("5500"));
        Machinery updated = machineryRepository.save(saved);
        entityManager.flush();

        Machinery found = machineryRepository.findById(updated.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Mantenimiento", found.getStatus());
        assertEquals(new BigDecimal("5500"), found.getPricePerDay());
    }

    @Test
    void count_shouldReturnCorrectCount() {
        entityManager.persist(machinery1);
        entityManager.persist(machinery2);
        entityManager.flush();

        long count = machineryRepository.count();

        assertEquals(2, count);
    }
}
