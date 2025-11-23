package com.briamcarrasco.arriendomaquinaria.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para recibir la creación/actualización de una reseña vía JSON.
 * Incluye la calificación y el comentario de la reseña.
 */
@Data
public class ReviewRequest {

    /**
     * Calificación de la reseña (valor entre 1 y 5).
     */
    @Min(1)
    @Max(5)
    private Integer rating;

    /**
     * Comentario de la reseña.
     */
    @NotBlank
    private String comment;
}