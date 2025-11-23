package com.briamcarrasco.arriendomaquinaria.repository;

import com.briamcarrasco.arriendomaquinaria.model.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio para la entidad Review.
 * Proporciona métodos para acceder y gestionar reseñas en la base de datos.
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Obtiene la lista de reseñas asociadas a una maquinaria.
     *
     * @param machineryId identificador de la maquinaria
     * @return lista de reseñas de la maquinaria
     */
    List<Review> findByMachineryId(Long machineryId);

    /**
     * Busca una reseña por el identificador de maquinaria y usuario.
     *
     * @param machineryId identificador de la maquinaria
     * @param userId      identificador del usuario
     * @return un Optional con la reseña encontrada o vacío si no existe
     */
    Optional<Review> findByMachineryIdAndUserId(Long machineryId, Long userId);

    /**
     * Calcula el promedio de calificaciones para una maquinaria.
     *
     * @param machineryId identificador de la maquinaria
     * @return promedio de calificaciones, o 0 si no existen reseñas
     */
    @Query("SELECT COALESCE(AVG(r.rating),0) FROM Review r WHERE r.machinery.id = :machineryId")
    Double findAverageRatingByMachinery(@Param("machineryId") Long machineryId);
}