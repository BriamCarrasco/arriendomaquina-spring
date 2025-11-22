package com.briamcarrasco.arriendomaquinaria.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para recibir la creación/actualización de una reseña vía JSON.
 */
@Data
public class ReviewRequest {

    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank
    private String comment;
}
