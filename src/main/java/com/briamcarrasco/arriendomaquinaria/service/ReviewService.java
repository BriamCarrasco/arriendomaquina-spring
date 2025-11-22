package com.briamcarrasco.arriendomaquinaria.service;

import com.briamcarrasco.arriendomaquinaria.model.Review;
import java.util.List;

public interface ReviewService {

    Review saveReview(Review review);

    Review getReviewById(Long id);

    void deleteReview(Long id);

    List<Review> getReviewsByMachinery(Long machineryId);

    Double getAverageRating(Long machineryId);

    Review upsertReview(Long machineryId, Long userId, Integer rating, String comment);
}
