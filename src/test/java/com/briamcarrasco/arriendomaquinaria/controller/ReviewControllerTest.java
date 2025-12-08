package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.dto.ReviewRequest;
import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.model.Review;
import com.briamcarrasco.arriendomaquinaria.model.User;
import com.briamcarrasco.arriendomaquinaria.repository.UserRepository;
import com.briamcarrasco.arriendomaquinaria.service.MachineryService;
import com.briamcarrasco.arriendomaquinaria.service.ReviewService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private MachineryService machineryService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ReviewController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------------------------------------------------
    // GET REVIEWS BY MACHINERY
    // -------------------------------------------------------------

    @Test
    void getReviewsByMachinery_returnsListOk() {
        Review review = new Review();
        review.setId(1L);
        when(reviewService.getReviewsByMachinery(10L)).thenReturn(List.of(review));

        ResponseEntity<List<Review>> response = controller.getReviewsByMachinery(10L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
    }

    // -------------------------------------------------------------
    // GET AVERAGE RATING
    // -------------------------------------------------------------

    @Test
    void getAverageRating_returnsOk() {
        when(reviewService.getAverageRating(5L)).thenReturn(4.5);

        ResponseEntity<Double> response = controller.getAverage(5L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(4.5, response.getBody());
    }

    // -------------------------------------------------------------
    // UPSERT REVIEW
    // -------------------------------------------------------------

    @Test
    void upsertReview_authenticationNull_returns401() {
        ReviewRequest request = new ReviewRequest();
        request.setRating(5);
        request.setComment("Test");

        ResponseEntity<Object> response = controller.upsertReview(1L, request, null);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("No autenticado", response.getBody());
    }

    @Test
    void upsertReview_notAuthenticated_returns401() {
        when(authentication.isAuthenticated()).thenReturn(false);

        ReviewRequest request = new ReviewRequest();
        request.setRating(5);
        request.setComment("Test");

        ResponseEntity<Object> response = controller.upsertReview(1L, request, authentication);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("No autenticado", response.getBody());
    }

    @Test
    void upsertReview_machineryNotFound_returns404() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(machineryService.findById(8L)).thenReturn(Optional.empty());

        ReviewRequest request = new ReviewRequest();
        request.setRating(4);
        request.setComment("ok");

        ResponseEntity<Object> response = controller.upsertReview(8L, request, authentication);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Maquinaria no encontrada", response.getBody());
    }

    @Test
    void upsertReview_userNotFound_returns404() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("ghost");
        when(machineryService.findById(1L)).thenReturn(Optional.of(new Machinery()));
        when(userRepository.findByUsername("ghost")).thenReturn(null);

        ReviewRequest request = new ReviewRequest();
        request.setRating(5);
        request.setComment("todo bien");

        ResponseEntity<Object> response = controller.upsertReview(1L, request, authentication);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Usuario no encontrado", response.getBody());
    }

    @Test
    @SuppressWarnings("unchecked")
    void upsertReview_success_returns200WithPayload() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("briam");
        when(machineryService.findById(2L)).thenReturn(Optional.of(new Machinery()));

        User user = new User();
        user.setId(10L);
        user.setUsername("briam");
        when(userRepository.findByUsername("briam")).thenReturn(user);

        Review review = new Review();
        review.setId(99L);
        review.setRating(5);
        review.setComment("Excelente!");
        when(reviewService.upsertReview(2L, 10L, 5, "Excelente!")).thenReturn(review);
        when(reviewService.getAverageRating(2L)).thenReturn(4.8);

        ReviewRequest request = new ReviewRequest();
        request.setRating(5);
        request.setComment("Excelente!");

        ResponseEntity<Object> response = controller.upsertReview(2L, request, authentication);

        assertEquals(200, response.getStatusCode().value());
        Map<String, Object> payload = (Map<String, Object>) response.getBody();
        assertEquals(99L, payload.get("id"));
        assertEquals(5, payload.get("rating"));
        assertEquals("Excelente!", payload.get("comment"));
        assertEquals("briam", payload.get("user"));
        assertEquals(4.8, payload.get("averageRating"));
    }
}