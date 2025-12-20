package com.briamcarrasco.arriendomaquinaria.repository;

import com.briamcarrasco.arriendomaquinaria.model.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;

@DataJpaTest
class StatusRepositoryTest {

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testSaveStatus() {
        Status status = new Status();
        status.setName("Disponible");

        Status savedStatus = statusRepository.save(status);

        assertNotNull(savedStatus);
        assertNotNull(savedStatus.getId());
        assertEquals("Disponible", savedStatus.getName());
    }

    @Test
    void testFindById() {
        Status status = new Status();
        status.setName("Operativa");
        Status savedStatus = entityManager.persistAndFlush(status);

        Optional<Status> foundStatus = statusRepository.findById(savedStatus.getId());

        assertTrue(foundStatus.isPresent());
        assertEquals(savedStatus.getId(), foundStatus.get().getId());
        assertEquals("Operativa", foundStatus.get().getName());
    }

    @Test
    void testFindByName() {
        Status status = new Status();
        status.setName("En Mantenimiento");
        entityManager.persistAndFlush(status);

        Status foundStatus = statusRepository.findByName("En Mantenimiento");

        assertNotNull(foundStatus);
        assertEquals("En Mantenimiento", foundStatus.getName());
    }

    @Test
    void testFindByNameReturnsNull() {
        Status foundStatus = statusRepository.findByName("Estado Inexistente");
        assertNull(foundStatus);
    }

    @Test
    void testExistsByName() {
        Status status = new Status();
        status.setName("Arrendada");
        entityManager.persistAndFlush(status);

        boolean exists = statusRepository.existsByName("Arrendada");
        assertTrue(exists);
    }

    @Test
    void testExistsByNameReturnsFalse() {
        boolean exists = statusRepository.existsByName("Estado No Existe");
        assertFalse(exists);
    }

    @Test
    void testFindAll() {
        Status status1 = new Status();
        status1.setName("Disponible");
        entityManager.persistAndFlush(status1);

        Status status2 = new Status();
        status2.setName("Ocupada");
        entityManager.persistAndFlush(status2);

        List<Status> allStatus = statusRepository.findAll();

        assertNotNull(allStatus);
        assertTrue(allStatus.size() >= 2);
    }

    @Test
    void testDeleteStatus() {
        Status status = new Status();
        status.setName("Fuera de Servicio");
        Status savedStatus = entityManager.persistAndFlush(status);

        statusRepository.deleteById(savedStatus.getId());

        Optional<Status> deletedStatus = statusRepository.findById(savedStatus.getId());
        assertFalse(deletedStatus.isPresent());
    }

    @Test
    void testCount() {
        long countBefore = statusRepository.count();

        Status status = new Status();
        status.setName("Nuevo Estado");
        entityManager.persistAndFlush(status);

        long countAfter = statusRepository.count();

        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    void testExistsById() {
        Status status = new Status();
        status.setName("Test Status");
        Status savedStatus = entityManager.persistAndFlush(status);

        assertTrue(statusRepository.existsById(savedStatus.getId()));
        assertFalse(statusRepository.existsById(999L));
    }

    @Test
    void testUpdateStatus() {
        Status status = new Status();
        status.setName("Estado Original");
        Status savedStatus = entityManager.persistAndFlush(status);

        savedStatus.setName("Estado Modificado");
        Status updatedStatus = statusRepository.save(savedStatus);

        assertEquals("Estado Modificado", updatedStatus.getName());
        assertNotEquals("Estado Original", updatedStatus.getName());
    }

    @Test
    void testSaveMultipleStatus() {
        Status status1 = new Status();
        status1.setName("Estado 1");

        Status status2 = new Status();
        status2.setName("Estado 2");

        Status status3 = new Status();
        status3.setName("Estado 3");

        statusRepository.save(status1);
        statusRepository.save(status2);
        statusRepository.save(status3);

        List<Status> allStatus = statusRepository.findAll();
        assertTrue(allStatus.size() >= 3);
    }

    @Test
    void testFindByNameCaseSensitive() {
        Status status = new Status();
        status.setName("DISPONIBLE");
        entityManager.persistAndFlush(status);

        Status foundUpper = statusRepository.findByName("DISPONIBLE");
        Status foundLower = statusRepository.findByName("disponible");

        assertNotNull(foundUpper);
        assertNull(foundLower);
    }

    @Test
    void testExistsByNameCaseSensitive() {
        Status status = new Status();
        status.setName("Operativa");
        entityManager.persistAndFlush(status);

        assertTrue(statusRepository.existsByName("Operativa"));
        assertFalse(statusRepository.existsByName("operativa"));
        assertFalse(statusRepository.existsByName("OPERATIVA"));
    }

    @Test
    void testFindByIdReturnsEmpty() {
        Optional<Status> status = statusRepository.findById(999L);
        assertFalse(status.isPresent());
    }

    @Test
    void testDeleteAllStatus() {
        Status status1 = new Status();
        status1.setName("Delete Test 1");
        Status saved1 = entityManager.persistAndFlush(status1);

        Status status2 = new Status();
        status2.setName("Delete Test 2");
        Status saved2 = entityManager.persistAndFlush(status2);

        statusRepository.deleteAll(List.of(saved1, saved2));

        assertFalse(statusRepository.existsById(saved1.getId()));
        assertFalse(statusRepository.existsById(saved2.getId()));
    }

    @Test
    void testStatusWithLongName() {
        String longName = "Estado con un nombre extremadamente largo para verificar el límite de caracteres";
        Status status = new Status();
        status.setName(longName);

        Status savedStatus = statusRepository.save(status);

        assertEquals(longName, savedStatus.getName());
    }

    @Test
    void testStatusWithSpecialCharacters() {
        Status status = new Status();
        status.setName("Estado - En Revisión (Pendiente)");

        Status savedStatus = statusRepository.save(status);

        assertEquals("Estado - En Revisión (Pendiente)", savedStatus.getName());
    }

    @Test
    void testMultipleFindByNameCalls() {
        Status status = new Status();
        status.setName("Estado Único");
        entityManager.persistAndFlush(status);

        Status found1 = statusRepository.findByName("Estado Único");
        Status found2 = statusRepository.findByName("Estado Único");

        assertNotNull(found1);
        assertNotNull(found2);
        assertEquals(found1.getId(), found2.getId());
        assertEquals(found1.getName(), found2.getName());
    }

    @Test
    void testFindAllReturnsOrderedList() {
        statusRepository.deleteAll();

        Status status1 = new Status();
        status1.setName("A - Primer Estado");
        entityManager.persistAndFlush(status1);

        Status status2 = new Status();
        status2.setName("B - Segundo Estado");
        entityManager.persistAndFlush(status2);

        Status status3 = new Status();
        status3.setName("C - Tercer Estado");
        entityManager.persistAndFlush(status3);

        List<Status> allStatus = statusRepository.findAll();

        assertEquals(3, allStatus.size());
    }
}
