package com.briamcarrasco.arriendomaquinaria.service;

import com.briamcarrasco.arriendomaquinaria.model.Review;
import java.util.List;

/**
 * Interfaz para el servicio de gestión de reseñas de maquinaria.
 * 
 * Define operaciones para guardar, obtener, eliminar y actualizar reseñas,
 * así como calcular el promedio de calificaciones y obtener reseñas por
 * maquinaria.
 */
public interface ReviewService {

    /**
     * Guarda una reseña en el sistema.
     *
     * @param review objeto Review a guardar
     * @return la reseña guardada
     */
    Review saveReview(Review review);

    /**
     * Obtiene una reseña por su identificador.
     *
     * @param id identificador de la reseña
     * @return la reseña encontrada o {@code null} si no existe
     */
    Review getReviewById(Long id);

    /**
     * Elimina una reseña por su identificador.
     *
     * @param id identificador de la reseña a eliminar
     */
    void deleteReview(Long id);

    /**
     * Obtiene todas las reseñas asociadas a una maquinaria.
     *
     * @param machineryId identificador de la maquinaria
     * @return lista de reseñas de la maquinaria
     */
    List<Review> getReviewsByMachinery(Long machineryId);

    /**
     * Calcula el promedio de calificaciones para una maquinaria.
     *
     * @param machineryId identificador de la maquinaria
     * @return promedio de calificaciones
     */
    Double getAverageRating(Long machineryId);

    /**
     * Crea o actualiza una reseña para una maquinaria y usuario específicos.
     *
     * @param machineryId identificador de la maquinaria
     * @param userId      identificador del usuario
     * @param rating      calificación
     * @param comment     comentario de la reseña
     * @return la reseña creada o actualizada
     */
    Review upsertReview(Long machineryId, Long userId, Integer rating, String comment);
}