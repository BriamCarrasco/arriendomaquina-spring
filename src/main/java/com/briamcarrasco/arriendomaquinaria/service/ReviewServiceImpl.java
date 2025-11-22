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

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MachineryRepository machineryRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
            MachineryRepository machineryRepository,
            UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.machineryRepository = machineryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public List<Review> getReviewsByMachinery(Long machineryId) {
        return reviewRepository.findByMachineryId(machineryId);
    }

    @Override
    public Double getAverageRating(Long machineryId) {
        return reviewRepository.findAverageRatingByMachinery(machineryId);
    }

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
