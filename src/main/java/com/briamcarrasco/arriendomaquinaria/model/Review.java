package com.briamcarrasco.arriendomaquinaria.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * Entidad que representa una reseña realizada por un usuario sobre una
 * maquinaria.
 */
@Entity
@Data
@Table(name = "tb_reviews")
public class Review {

    /**
     * Identificador único de la reseña.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Maquinaria a la cual pertenece la reseña.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machinery_id", nullable = false)
    private Machinery machinery;

    /**
     * Usuario que realizó la reseña.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Puntuación otorgada a la maquinaria (valor mínimo 1, máximo 5).
     */
    @Min(1)
    @Max(5)
    @Column(name = "rating", nullable = false)
    private Integer rating;

    /**
     * Comentario descriptivo de la reseña.
     */
    @Column(name = "comment", length = 1000)
    private String comment;

    /**
     * Fecha y hora de creación del registro.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de la última actualización del registro.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
