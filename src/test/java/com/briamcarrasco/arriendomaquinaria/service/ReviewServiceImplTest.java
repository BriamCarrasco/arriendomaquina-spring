package com.briamcarrasco.arriendomaquinaria.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.model.Review;
import com.briamcarrasco.arriendomaquinaria.model.User;
import com.briamcarrasco.arriendomaquinaria.repository.MachineryRepository;
import com.briamcarrasco.arriendomaquinaria.repository.ReviewRepository;
import com.briamcarrasco.arriendomaquinaria.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MachineryRepository machineryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewServiceImpl service;

    @Test
    void saveReview_delegatesToRepository_andReturnsSaved() {
        Review r = new Review();
        r.setRating(3);
        when(reviewRepository.save(r)).thenReturn(r);

        Review res = service.saveReview(r);

        assertSame(r, res);
        verify(reviewRepository).save(r);
    }

    @Test
    void getReviewById_whenPresent_returnsReview_elseNull() {
        Review r = new Review();
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(r));
        when(reviewRepository.findById(2L)).thenReturn(Optional.empty());

        assertSame(r, service.getReviewById(1L));
        assertNull(service.getReviewById(2L));

        verify(reviewRepository, times(2)).findById(anyLong());
    }

    @Test
    void deleteReview_callsRepositoryDeleteById() {
        service.deleteReview(11L);
        verify(reviewRepository).deleteById(11L);
    }

    @Test
    void getReviewsByMachinery_returnsListFromRepo() {
        Review r1 = new Review();
        Review r2 = new Review();
        List<Review> list = Arrays.asList(r1, r2);
        when(reviewRepository.findByMachineryId(5L)).thenReturn(list);

        List<Review> res = service.getReviewsByMachinery(5L);

        assertSame(list, res);
        assertEquals(2, res.size());
        verify(reviewRepository).findByMachineryId(5L);
    }

    @Test
    void getAverageRating_returnsRepositoryValue() {
        when(reviewRepository.findAverageRatingByMachinery(7L)).thenReturn(4.25);
        Double avg = service.getAverageRating(7L);
        assertEquals(4.25, avg);
        verify(reviewRepository).findAverageRatingByMachinery(7L);
    }

    @Test
    void upsertReview_whenExisting_updatesRatingAndComment_thenSaves() {
        Review existing = new Review();
        existing.setRating(2);
        existing.setComment("old");
        when(reviewRepository.findByMachineryIdAndUserId(3L, 9L)).thenReturn(Optional.of(existing));
        when(reviewRepository.save(any(Review.class))).thenAnswer(inv -> inv.getArgument(0));

        Review res = service.upsertReview(3L, 9L, 5, "new comment");

        assertSame(existing, res);
        assertEquals(5, res.getRating());
        assertEquals("new comment", res.getComment());
        verify(reviewRepository).findByMachineryIdAndUserId(3L, 9L);
        verify(reviewRepository).save(existing);
    }

    @Test
    void upsertReview_whenNotExisting_andMachineryOrUserMissing_throws() {
        when(reviewRepository.findByMachineryIdAndUserId(4L, 2L)).thenReturn(Optional.empty());
        when(machineryRepository.findById(4L)).thenReturn(Optional.empty());
        when(userRepository.findById(2L)).thenReturn(Optional.of(new User()));

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> service.upsertReview(4L, 2L, 3, "c"));
        assertEquals("Machinery o User no encontrados para crear reseña", ex1.getMessage());

        // reverse: machinery present, user missing
        when(machineryRepository.findById(4L)).thenReturn(Optional.of(new Machinery()));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> service.upsertReview(4L, 2L, 4, "c2"));
        assertEquals("Machinery o User no encontrados para crear reseña", ex2.getMessage());

        verify(reviewRepository, times(2)).findByMachineryIdAndUserId(4L, 2L);
    }

    @Test
    void upsertReview_whenNotExisting_createsReviewAndSaves() {
        when(reviewRepository.findByMachineryIdAndUserId(8L, 6L)).thenReturn(Optional.empty());

        Machinery mach = new Machinery();
        User user = new User();
        when(machineryRepository.findById(8L)).thenReturn(Optional.of(mach));
        when(userRepository.findById(6L)).thenReturn(Optional.of(user));
        when(reviewRepository.save(any(Review.class))).thenAnswer(inv -> inv.getArgument(0));

        Review res = service.upsertReview(8L, 6L, 5, "great");

        assertNotNull(res);
        assertSame(mach, res.getMachinery());
        assertSame(user, res.getUser());
        assertEquals(5, res.getRating());
        assertEquals("great", res.getComment());
        verify(reviewRepository).save(res);
    }
}