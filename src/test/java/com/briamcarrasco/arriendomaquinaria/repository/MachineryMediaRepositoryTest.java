package com.briamcarrasco.arriendomaquinaria.repository;

import com.briamcarrasco.arriendomaquinaria.model.MachineryMedia;
import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@DataJpaTest
class MachineryMediaRepositoryTest {

    @Autowired
    private MachineryMediaRepository machineryMediaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testSaveMachineryMedia() {
        Category category = new Category();
        category.setName("Construcción");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Excavadora");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("10000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryMedia media = new MachineryMedia();
        media.setImgUrl("/images/excavator1.jpg");
        media.setVidUrl("/videos/excavator1.mp4");
        media.setMachinery(machinery);

        MachineryMedia savedMedia = machineryMediaRepository.save(media);

        assertNotNull(savedMedia);
        assertTrue(savedMedia.getId() > 0);
        assertEquals("/images/excavator1.jpg", savedMedia.getImgUrl());
        assertEquals("/videos/excavator1.mp4", savedMedia.getVidUrl());
    }

    @Test
    void testFindById() {
        Category category = new Category();
        category.setName("Industrial");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Grúa");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("15000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryMedia media = new MachineryMedia();
        media.setImgUrl("/images/crane.jpg");
        media.setMachinery(machinery);
        MachineryMedia savedMedia = entityManager.persistAndFlush(media);

        Optional<MachineryMedia> foundMedia = machineryMediaRepository.findById(savedMedia.getId());

        assertTrue(foundMedia.isPresent());
        assertEquals(savedMedia.getId(), foundMedia.get().getId());
        assertEquals("/images/crane.jpg", foundMedia.get().getImgUrl());
    }

    @Test
    void testFindByMachineryId() {
        Category category = new Category();
        category.setName("Agricultura");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Tractor");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("8000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryMedia media1 = new MachineryMedia();
        media1.setImgUrl("/images/tractor1.jpg");
        media1.setMachinery(machinery);
        entityManager.persistAndFlush(media1);

        MachineryMedia media2 = new MachineryMedia();
        media2.setImgUrl("/images/tractor2.jpg");
        media2.setVidUrl("/videos/tractor_demo.mp4");
        media2.setMachinery(machinery);
        entityManager.persistAndFlush(media2);

        List<MachineryMedia> mediaList = machineryMediaRepository.findByMachineryId(machinery.getId());

        assertNotNull(mediaList);
        assertEquals(2, mediaList.size());
    }

    @Test
    void testFindByMachineryIdReturnsEmpty() {
        List<MachineryMedia> mediaList = machineryMediaRepository.findByMachineryId(999L);

        assertNotNull(mediaList);
        assertTrue(mediaList.isEmpty());
    }

    @Test
    void testFindAll() {
        Category category = new Category();
        category.setName("Transporte");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Montacargas");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("5000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryMedia media1 = new MachineryMedia();
        media1.setImgUrl("/images/forklift1.jpg");
        media1.setMachinery(machinery);
        entityManager.persistAndFlush(media1);

        MachineryMedia media2 = new MachineryMedia();
        media2.setImgUrl("/images/forklift2.jpg");
        media2.setMachinery(machinery);
        entityManager.persistAndFlush(media2);

        List<MachineryMedia> allMedia = machineryMediaRepository.findAll();

        assertNotNull(allMedia);
        assertTrue(allMedia.size() >= 2);
    }

    @Test
    void testDeleteMachineryMedia() {
        Category category = new Category();
        category.setName("Minería");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Bulldozer");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("12000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryMedia media = new MachineryMedia();
        media.setImgUrl("/images/bulldozer.jpg");
        media.setMachinery(machinery);
        MachineryMedia savedMedia = entityManager.persistAndFlush(media);

        machineryMediaRepository.deleteById(savedMedia.getId());

        Optional<MachineryMedia> deletedMedia = machineryMediaRepository.findById(savedMedia.getId());
        assertFalse(deletedMedia.isPresent());
    }

    @Test
    void testCount() {
        Category category = new Category();
        category.setName("Carpintería");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Sierra");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("3000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        long countBefore = machineryMediaRepository.count();

        MachineryMedia media = new MachineryMedia();
        media.setImgUrl("/images/saw.jpg");
        media.setMachinery(machinery);
        entityManager.persistAndFlush(media);

        long countAfter = machineryMediaRepository.count();

        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    void testExistsById() {
        Category category = new Category();
        category.setName("Electricidad");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Generador");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("4000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryMedia media = new MachineryMedia();
        media.setImgUrl("/images/generator.jpg");
        media.setMachinery(machinery);
        MachineryMedia savedMedia = entityManager.persistAndFlush(media);

        assertTrue(machineryMediaRepository.existsById(savedMedia.getId()));
        assertFalse(machineryMediaRepository.existsById(999L));
    }

    @Test
    void testUpdateMachineryMedia() {
        Category category = new Category();
        category.setName("Pavimentación");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Compactadora");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("7000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryMedia media = new MachineryMedia();
        media.setImgUrl("/images/old_compactor.jpg");
        media.setMachinery(machinery);
        MachineryMedia savedMedia = entityManager.persistAndFlush(media);

        savedMedia.setImgUrl("/images/new_compactor.jpg");
        savedMedia.setVidUrl("/videos/compactor_demo.mp4");
        MachineryMedia updatedMedia = machineryMediaRepository.save(savedMedia);

        assertEquals("/images/new_compactor.jpg", updatedMedia.getImgUrl());
        assertEquals("/videos/compactor_demo.mp4", updatedMedia.getVidUrl());
    }

    @Test
    void testSaveMediaWithOnlyImage() {
        Category category = new Category();
        category.setName("Jardinería");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Cortacésped");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("2000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryMedia media = new MachineryMedia();
        media.setImgUrl("/images/lawnmower.jpg");
        media.setMachinery(machinery);

        MachineryMedia savedMedia = machineryMediaRepository.save(media);

        assertNotNull(savedMedia.getImgUrl());
        assertNull(savedMedia.getVidUrl());
    }

    @Test
    void testSaveMediaWithOnlyVideo() {
        Category category = new Category();
        category.setName("Limpieza");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Hidrolavadora");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("2500"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryMedia media = new MachineryMedia();
        media.setVidUrl("/videos/pressure_washer.mp4");
        media.setMachinery(machinery);

        MachineryMedia savedMedia = machineryMediaRepository.save(media);

        assertNull(savedMedia.getImgUrl());
        assertNotNull(savedMedia.getVidUrl());
    }

    @Test
    void testMultipleMediaForSameMachinery() {
        Category category = new Category();
        category.setName("Demolición");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Martillo Hidráulico");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("9000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryMedia media1 = new MachineryMedia();
        media1.setImgUrl("/images/hammer1.jpg");
        media1.setMachinery(machinery);
        entityManager.persistAndFlush(media1);

        MachineryMedia media2 = new MachineryMedia();
        media2.setImgUrl("/images/hammer2.jpg");
        media2.setMachinery(machinery);
        entityManager.persistAndFlush(media2);

        MachineryMedia media3 = new MachineryMedia();
        media3.setVidUrl("/videos/hammer_action.mp4");
        media3.setMachinery(machinery);
        entityManager.persistAndFlush(media3);

        List<MachineryMedia> mediaList = machineryMediaRepository.findByMachineryId(machinery.getId());

        assertEquals(3, mediaList.size());
        assertEquals(machinery.getId(), mediaList.get(0).getMachinery().getId());
        assertEquals(machinery.getId(), mediaList.get(1).getMachinery().getId());
        assertEquals(machinery.getId(), mediaList.get(2).getMachinery().getId());
    }

    @Test
    void testFindByIdReturnsEmpty() {
        Optional<MachineryMedia> media = machineryMediaRepository.findById(999L);
        assertFalse(media.isPresent());
    }

    @Test
    void testDeleteAll() {
        Category category = new Category();
        category.setName("Test Category");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Test Machinery");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("1000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryMedia media1 = new MachineryMedia();
        media1.setImgUrl("/images/test1.jpg");
        media1.setMachinery(machinery);
        entityManager.persistAndFlush(media1);

        MachineryMedia media2 = new MachineryMedia();
        media2.setImgUrl("/images/test2.jpg");
        media2.setMachinery(machinery);
        entityManager.persistAndFlush(media2);

        List<MachineryMedia> mediaToDelete = machineryMediaRepository.findByMachineryId(machinery.getId());
        machineryMediaRepository.deleteAll(mediaToDelete);

        List<MachineryMedia> remaining = machineryMediaRepository.findByMachineryId(machinery.getId());
        assertTrue(remaining.isEmpty());
    }
}
