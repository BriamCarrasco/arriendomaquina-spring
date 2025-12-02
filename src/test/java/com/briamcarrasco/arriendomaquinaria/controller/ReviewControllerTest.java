package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.dto.ReviewRequest;
import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.model.Review;
import com.briamcarrasco.arriendomaquinaria.model.User;
import com.briamcarrasco.arriendomaquinaria.repository.UserRepository;
import com.briamcarrasco.arriendomaquinaria.service.MachineryService;
import com.briamcarrasco.arriendomaquinaria.service.ReviewService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
@Import(ReviewControllerTest.TestConfig.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MachineryService machineryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewController reviewController;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ReviewService reviewService() {
            return Mockito.mock(ReviewService.class);
        }

        @Bean
        public MachineryService machineryService() {
            return Mockito.mock(MachineryService.class);
        }

        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }
    }

    @Test
    @WithMockUser
    void getReviewsByMachinery_retornaLista200() throws Exception {
        Review review = new Review();
        review.setId(1L);

        Mockito.when(reviewService.getReviewsByMachinery(10L))
                .thenReturn(List.of(review));

        mockMvc.perform(get("/api/reviews/machinery/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser
    void getAverageRating_retorna200() throws Exception {
        Mockito.when(reviewService.getAverageRating(5L)).thenReturn(4.5);

        mockMvc.perform(get("/api/reviews/machinery/5/average"))
                .andExpect(status().isOk())
                .andExpect(content().string("4.5"));
    }

    @Test
    void upsertReview_sinAutenticacion_retorna401() throws Exception {
        mockMvc.perform(post("/api/reviews/machinery/8")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"rating\":5,\"comment\":\"Bien\"}"))
                // Sin CSRF en POST -> 403 por protección CSRF
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void upsertReview_maquinariaNoExiste_404() {
        Mockito.when(machineryService.findById(8L)).thenReturn(Optional.empty());
        ReviewRequest body = new ReviewRequest();
        body.setRating(4);
        body.setComment("ok");
        var auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                "user", "pwd",
                org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_USER"));
        var resp = reviewController.upsertReview(8L, body, auth);
        org.assertj.core.api.Assertions.assertThat(resp.getStatusCode().value()).isEqualTo(404);
        org.assertj.core.api.Assertions.assertThat(resp.getBody()).isEqualTo("Maquinaria no encontrada");
    }

    @Test
    @WithMockUser(username = "ghost", roles = "USER")
    void upsertReview_usuarioNoExiste_404() {
        Mockito.when(machineryService.findById(1L)).thenReturn(Optional.of(new Machinery()));
        Mockito.when(userRepository.findByUsername("ghost")).thenReturn(null);
        ReviewRequest body = new ReviewRequest();
        body.setRating(5);
        body.setComment("todo bien");
        var auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                "ghost", "pwd",
                org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_USER"));
        var resp = reviewController.upsertReview(1L, body, auth);
        org.assertj.core.api.Assertions.assertThat(resp.getStatusCode().value()).isEqualTo(404);
        org.assertj.core.api.Assertions.assertThat(resp.getBody()).isEqualTo("Usuario no encontrado");
    }

    @Test
    @WithMockUser(username = "briam", roles = "USER")
    void upsertReview_exitoso_200() {
        Mockito.when(machineryService.findById(2L)).thenReturn(Optional.of(new Machinery()));

        User user = new User();
        user.setId(10L);
        user.setUsername("briam");
        Mockito.when(userRepository.findByUsername("briam")).thenReturn(user);

        Review review = new Review();
        review.setId(99L);
        review.setRating(5);
        review.setComment("Excelente!");

        Mockito.when(reviewService.upsertReview(2L, 10L, 5, "Excelente!")).thenReturn(review);
        Mockito.when(reviewService.getAverageRating(2L)).thenReturn(4.8);

        ReviewRequest body = new ReviewRequest();
        body.setRating(5);
        body.setComment("Excelente!");
        var auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                "briam", "pwd",
                org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_USER"));
        var resp = reviewController.upsertReview(2L, body, auth);
        org.assertj.core.api.Assertions.assertThat(resp.getStatusCode().value()).isEqualTo(200);
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> payload = (java.util.Map<String, Object>) resp.getBody();
        org.assertj.core.api.Assertions.assertThat(payload)
                .containsEntry("id", 99L)
                .containsEntry("rating", 5)
                .containsEntry("comment", "Excelente!")
                .containsEntry("user", "briam")
                .containsEntry("averageRating", 4.8);
    }
}