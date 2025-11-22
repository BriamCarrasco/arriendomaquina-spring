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

    @GetMapping("/machinery/{machineryId}")
    public ResponseEntity<List<Review>> getReviewsByMachinery(@PathVariable Long machineryId) {
        return ResponseEntity.ok(reviewService.getReviewsByMachinery(machineryId));
    }

    @GetMapping("/machinery/{machineryId}/average")
    public ResponseEntity<Double> getAverage(@PathVariable Long machineryId) {
        return ResponseEntity.ok(reviewService.getAverageRating(machineryId));
    }

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