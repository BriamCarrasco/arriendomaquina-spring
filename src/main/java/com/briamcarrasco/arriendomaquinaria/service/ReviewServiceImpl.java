package com.briamcarrasco.arriendomaquinaria.service;

import org.springframework.stereotype.Service;
import com.briamcarrasco.arriendomaquinaria.repository.ReviewRepository;
import com.briamcarrasco.arriendomaquinaria.repository.MachineryRepository;
import com.briamcarrasco.arriendomaquinaria.repository.UserRepository;
import com.briamcarrasco.arriendomaquinaria.model.Review;
import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de reseñas de maquinaria.
 * 
 * Permite guardar, obtener, eliminar y actualizar reseñas, así como calcular el
 * promedio de calificaciones
 * y obtener reseñas por maquinaria.
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MachineryRepository machineryRepository;
    private final UserRepository userRepository;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param reviewRepository    repositorio de reseñas
     * @param machineryRepository repositorio de maquinaria
     * @param userRepository      repositorio de usuarios
     */
    public ReviewServiceImpl(ReviewRepository reviewRepository,
            MachineryRepository machineryRepository,
            UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.machineryRepository = machineryRepository;
        this.userRepository = userRepository;
    }

    /**
     * Guarda una reseña en el sistema.
     *
     * @param review objeto Review a guardar
     * @return la reseña guardada
     */
    @Override
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    /**
     * Obtiene una reseña por su identificador.
     *
     * @param id identificador de la reseña
     * @return la reseña encontrada o {@code null} si no existe
     */
    @Override
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    /**
     * Elimina una reseña por su identificador.
     *
     * @param id identificador de la reseña a eliminar
     */
    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    /**
     * Obtiene todas las reseñas asociadas a una maquinaria.
     *
     * @param machineryId identificador de la maquinaria
     * @return lista de reseñas de la maquinaria
     */
    @Override
    public List<Review> getReviewsByMachinery(Long machineryId) {
        return reviewRepository.findByMachineryId(machineryId);
    }

    /**
     * Calcula el promedio de calificaciones para una maquinaria.
     *
     * @param machineryId identificador de la maquinaria
     * @return promedio de calificaciones
     */
    @Override
    public Double getAverageRating(Long machineryId) {
        return reviewRepository.findAverageRatingByMachinery(machineryId);
    }

    /**
     * Crea o actualiza una reseña para una maquinaria y usuario específicos.
     *
     * @param machineryId identificador de la maquinaria
     * @param userId      identificador del usuario
     * @param rating      calificación
     * @param comment     comentario de la reseña
     * @return la reseña creada o actualizada
     * @throws IllegalArgumentException si la maquinaria o el usuario no existen
     */
    @Override
    public Review upsertReview(Long machineryId, Long userId, Integer rating, String comment) {
        Optional<Review> existingOpt = reviewRepository.findByMachineryIdAndUserId(machineryId, userId);
        Review review;
        if (existingOpt.isPresent()) {
            review = existingOpt.get();
            review.setRating(rating);
            review.setComment(comment);
        } else {
            Machinery machinery = machineryRepository.findById(machineryId).orElse(null);
            User user = userRepository.findById(userId).orElse(null);
            if (machinery == null || user == null) {
                throw new IllegalArgumentException("Machinery o User no encontrados para crear reseña");
            }
            review = new Review();
            review.setMachinery(machinery);
            review.setUser(user);
            review.setRating(rating);
            review.setComment(comment);
        }
        return reviewRepository.save(review);
    }

}