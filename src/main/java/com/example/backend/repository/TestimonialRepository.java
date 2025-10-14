package com.example.backend.repository;

import com.example.backend.entity.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial, UUID> {
    List<Testimonial> findByUserIdAndIsApprovedTrueOrderBySortOrder(UUID userId);
    List<Testimonial> findByUserIdAndIsFeaturedTrue(UUID userId);
    List<Testimonial> findByRating(Integer rating);
    List<Testimonial> findByUserIdOrderByTestimonialDateDesc(UUID userId);
}
