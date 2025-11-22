package com.briamcarrasco.arriendomaquinaria.repository;

import com.briamcarrasco.arriendomaquinaria.model.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMachineryId(Long machineryId);

    Optional<Review> findByMachineryIdAndUserId(Long machineryId, Long userId);

    @Query("SELECT COALESCE(AVG(r.rating),0) FROM Review r WHERE r.machinery.id = :machineryId")
    Double findAverageRatingByMachinery(@Param("machineryId") Long machineryId);
}
