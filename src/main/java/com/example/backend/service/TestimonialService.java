package com.example.backend.service;

import com.example.backend.entity.Testimonial;
import com.example.backend.repository.TestimonialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TestimonialService {

    @Autowired
    private TestimonialRepository testimonialRepository;

    public Testimonial saveTestimonial(Testimonial testimonial) {
        return testimonialRepository.save(testimonial);
    }

    public List<Testimonial> findApprovedByUserId(UUID userId) {
        return testimonialRepository.findByUserIdAndIsApprovedTrueOrderBySortOrder(userId);
    }

    public List<Testimonial> findFeaturedByUserId(UUID userId) {
        return testimonialRepository.findByUserIdAndIsFeaturedTrue(userId);
    }

    public List<Testimonial> findByUserId(UUID userId) {
        return testimonialRepository.findByUserIdOrderByTestimonialDateDesc(userId);
    }

    public Optional<Testimonial> findById(UUID id) {
        return testimonialRepository.findById(id);
    }

    public List<Testimonial> findByRating(Integer rating) {
        return testimonialRepository.findByRating(rating);
    }

    public Testimonial updateTestimonial(UUID id, Testimonial details) {
        return testimonialRepository.findById(id)
                .map(testimonial -> {
                    testimonial.setClientName(details.getClientName());
                    testimonial.setClientTitle(details.getClientTitle());
                    testimonial.setClientCompany(details.getClientCompany());
                    testimonial.setTestimonialText(details.getTestimonialText());
                    testimonial.setRating(details.getRating());
                    testimonial.setProjectName(details.getProjectName());
                    testimonial.setTestimonialDate(details.getTestimonialDate());
                    testimonial.setIsFeatured(details.getIsFeatured());
                    testimonial.setIsApproved(details.getIsApproved());
                    testimonial.setSortOrder(details.getSortOrder());
                    testimonial.setClientWebsite(details.getClientWebsite());
                    testimonial.setClientLinkedin(details.getClientLinkedin());
                    testimonial.setVideoTestimonialUrl(details.getVideoTestimonialUrl());
                    testimonial.setAudioTestimonialUrl(details.getAudioTestimonialUrl());
                    if (details.getClientImageUrl() != null) {
                        testimonial.setClientImageUrl(details.getClientImageUrl());
                    }
                    return testimonialRepository.save(testimonial);
                })
                .orElseThrow(() -> new RuntimeException("Testimonial not found with id: " + id));
    }

    public boolean deleteTestimonial(UUID id) {
        if (testimonialRepository.existsById(id)) {
            testimonialRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long getTestimonialsCount() {
        return testimonialRepository.count();
    }
}
