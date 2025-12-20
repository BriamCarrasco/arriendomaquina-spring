package com.briamcarrasco.arriendomaquinaria.repository;

import com.briamcarrasco.arriendomaquinaria.model.Category;
import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.model.Review;
import com.briamcarrasco.arriendomaquinaria.model.User;
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
 * Tests de integración para ReviewRepository.
 */
@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;
    private Machinery machinery;
    private Review review1;
    private Review review2;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        // Crear usuario
        user1 = new User();
        user1.setUsername("testuser");
        user1.setPassword("password");
        user1.setEmail("test@example.com");
        user1.setRole(User.Role.USER);
        user1 = entityManager.persistAndFlush(user1);

        user2 = new User();
        user2.setUsername("otheruser");
        user2.setPassword("password");
        user2.setEmail("other@example.com");
        user2.setRole(User.Role.USER);
        user2 = entityManager.persistAndFlush(user2);

        // Crear categoría
        Category category = new Category();
        category.setName("Excavadoras");
        category.setDescription("Descripción");
        category = entityManager.persistAndFlush(category);

        // Crear maquinaria
        machinery = new Machinery();
        machinery.setNameMachinery("Excavadora CAT");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("5000"));
        machinery.setCategory(category);
        machinery = entityManager.persistAndFlush(machinery);

        // Crear reviews
        review1 = new Review();
        review1.setMachinery(machinery);
        review1.setUser(user1);
        review1.setRating(5);
        review1.setComment("Excelente maquinaria");

        review2 = new Review();
        review2.setMachinery(machinery);
        review2.setUser(user2);
        review2.setRating(4);
        review2.setComment("Muy buena");
    }

    @Test
    void save_shouldPersistReview() {
        Review saved = reviewRepository.save(review1);

        assertNotNull(saved.getId());
        assertEquals(5, saved.getRating());
        assertEquals("Excelente maquinaria", saved.getComment());
    }

    @Test
    void findById_whenExists_shouldReturnReview() {
        Review saved = entityManager.persistAndFlush(review1);

        Optional<Review> found = reviewRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Excelente maquinaria", found.get().getComment());
    }

    @Test
    void findById_whenNotExists_shouldReturnEmpty() {
        Optional<Review> found = reviewRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void findAll_shouldReturnAllReviews() {
        entityManager.persist(review1);
        entityManager.persist(review2);
        entityManager.flush();

        List<Review> reviews = reviewRepository.findAll();

        assertEquals(2, reviews.size());
    }

    @Test
    void findByMachineryId_shouldReturnReviewsForMachinery() {
        entityManager.persist(review1);
        entityManager.persist(review2);
        entityManager.flush();

        List<Review> found = reviewRepository.findByMachineryId(machinery.getId());

        assertEquals(2, found.size());
    }

    @Test
    void findByMachineryId_whenNoReviews_shouldReturnEmpty() {
        List<Review> found = reviewRepository.findByMachineryId(999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void findByMachineryIdAndUserId_whenExists_shouldReturnReview() {
        entityManager.persistAndFlush(review1);

        Optional<Review> found = reviewRepository.findByMachineryIdAndUserId(
                machinery.getId(), user1.getId());

        assertTrue(found.isPresent());
        assertEquals("Excelente maquinaria", found.get().getComment());
    }

    @Test
    void findByMachineryIdAndUserId_whenNotExists_shouldReturnEmpty() {
        Optional<Review> found = reviewRepository.findByMachineryIdAndUserId(999L, 999L);

        assertFalse(found.isPresent());
    }

    @Test
    void findAverageRatingByMachinery_shouldCalculateAverage() {
        entityManager.persist(review1); // rating 5
        entityManager.persist(review2); // rating 4
        entityManager.flush();

        Double average = reviewRepository.findAverageRatingByMachinery(machinery.getId());

        assertNotNull(average);
        assertEquals(4.5, average, 0.01);
    }

    @Test
    void findAverageRatingByMachinery_whenNoReviews_shouldReturnZero() {
        Double average = reviewRepository.findAverageRatingByMachinery(machinery.getId());

        assertNotNull(average);
        assertEquals(0.0, average);
    }

    @Test
    void delete_shouldRemoveReview() {
        Review saved = entityManager.persistAndFlush(review1);
        Long id = saved.getId();

        reviewRepository.delete(saved);
        entityManager.flush();

        Optional<Review> found = reviewRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void update_shouldModifyReview() {
        Review saved = entityManager.persistAndFlush(review1);

        saved.setRating(3);
        saved.setComment("Comentario actualizado");
        Review updated = reviewRepository.save(saved);
        entityManager.flush();

        Review found = reviewRepository.findById(updated.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(3, found.getRating());
        assertEquals("Comentario actualizado", found.getComment());
    }

    @Test
    void count_shouldReturnCorrectCount() {
        entityManager.persist(review1);
        entityManager.persist(review2);
        entityManager.flush();

        long count = reviewRepository.count();

        assertEquals(2, count);
    }

    @Test
    void findByMachineryId_shouldOnlyReturnReviewsForSpecificMachinery() {
        // Crear otra maquinaria
        Category category = new Category();
        category.setName("Grúas");
        category = entityManager.persistAndFlush(category);

        Machinery otherMachinery = new Machinery();
        otherMachinery.setNameMachinery("Grúa");
        otherMachinery.setStatus("Disponible");
        otherMachinery.setPricePerDay(new BigDecimal("6000"));
        otherMachinery.setCategory(category);
        otherMachinery = entityManager.persistAndFlush(otherMachinery);

        Review otherReview = new Review();
        otherReview.setMachinery(otherMachinery);
        otherReview.setUser(user1);
        otherReview.setRating(5);
        otherReview.setComment("Otra reseña");

        entityManager.persist(review1);
        entityManager.persist(otherReview);
        entityManager.flush();

        List<Review> found = reviewRepository.findByMachineryId(machinery.getId());

        assertEquals(1, found.size());
        assertEquals("Excelente maquinaria", found.get(0).getComment());
    }
}
