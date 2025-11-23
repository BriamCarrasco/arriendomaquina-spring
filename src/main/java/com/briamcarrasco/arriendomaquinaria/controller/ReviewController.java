package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.dto.ReviewRequest;
import com.briamcarrasco.arriendomaquinaria.model.Review;
import com.briamcarrasco.arriendomaquinaria.model.User;
import com.briamcarrasco.arriendomaquinaria.service.ReviewService;
import com.briamcarrasco.arriendomaquinaria.repository.UserRepository;
import com.briamcarrasco.arriendomaquinaria.service.MachineryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para la gestión de reseñas de maquinaria.
 * 
 * Permite obtener reseñas por maquinaria, calcular el promedio de
 * calificaciones
 * y crear o actualizar reseñas asociadas a una maquinaria y usuario
 * autenticado.
 */
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final MachineryService machineryService; // para validar existencia
    private final UserRepository userRepository;

    public ReviewController(ReviewService reviewService,
            MachineryService machineryService,
            UserRepository userRepository) {
        this.reviewService = reviewService;
        this.machineryService = machineryService;
        this.userRepository = userRepository;
    }

    /**
     * Obtiene la lista de reseñas asociadas a una maquinaria.
     *
     * @param machineryId identificador de la maquinaria
     * @return lista de reseñas de la maquinaria
     */
    @GetMapping("/machinery/{machineryId}")
    public ResponseEntity<List<Review>> getReviewsByMachinery(@PathVariable Long machineryId) {
        return ResponseEntity.ok(reviewService.getReviewsByMachinery(machineryId));
    }

    /**
     * Obtiene el promedio de calificaciones de una maquinaria.
     *
     * @param machineryId identificador de la maquinaria
     * @return promedio de calificaciones
     */
    @GetMapping("/machinery/{machineryId}/average")
    public ResponseEntity<Double> getAverage(@PathVariable Long machineryId) {
        return ResponseEntity.ok(reviewService.getAverageRating(machineryId));
    }

    /**
     * Crea o actualiza una reseña para una maquinaria por el usuario autenticado.
     *
     * @param machineryId    identificador de la maquinaria
     * @param request        datos de la reseña (calificación y comentario)
     * @param authentication información de autenticación del usuario
     * @return respuesta con los datos de la reseña y el promedio actualizado
     */
    @PostMapping("/machinery/{machineryId}")
    public ResponseEntity<Object> upsertReview(@PathVariable Long machineryId,
            @Validated @RequestBody ReviewRequest request,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("No autenticado");
        }
        if (machineryService.findById(machineryId).isEmpty()) {
            return ResponseEntity.status(404).body("Maquinaria no encontrada");
        }
        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        Review review = reviewService.upsertReview(
                machineryId,
                user.getId(),
                request.getRating(),
                request.getComment());
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", review.getId());
        payload.put("rating", review.getRating());
        payload.put("comment", review.getComment());
        payload.put("user", user.getUsername());
        payload.put("averageRating", reviewService.getAverageRating(machineryId));
        return ResponseEntity.ok(payload);
    }
}