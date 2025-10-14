package com.example.backend.controller;

import com.example.backend.entity.Testimonial;
import com.example.backend.service.TestimonialService;
import com.example.backend.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/testimonials")
@CrossOrigin(origins = "*")
public class TestimonialController {

    @Autowired
    private TestimonialService testimonialService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Testimonial>> getTestimonialsByUserId(@PathVariable UUID userId) {
        List<Testimonial> testimonials = testimonialService.findByUserId(userId);
        return ResponseEntity.ok(testimonials);
    }

    @GetMapping("/user/{userId}/approved")
    public ResponseEntity<List<Testimonial>> getApprovedTestimonials(@PathVariable UUID userId) {
        List<Testimonial> testimonials = testimonialService.findApprovedByUserId(userId);
        return ResponseEntity.ok(testimonials);
    }

    @GetMapping("/user/{userId}/featured")
    public ResponseEntity<List<Testimonial>> getFeaturedTestimonials(@PathVariable UUID userId) {
        List<Testimonial> testimonials = testimonialService.findFeaturedByUserId(userId);
        return ResponseEntity.ok(testimonials);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Testimonial> getTestimonialById(@PathVariable UUID id) {
        Optional<Testimonial> testimonial = testimonialService.findById(id);
        return testimonial.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<Testimonial>> getTestimonialsByRating(@PathVariable Integer rating) {
        List<Testimonial> testimonials = testimonialService.findByRating(rating);
        return ResponseEntity.ok(testimonials);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createTestimonial(
            @RequestParam UUID userId,
            @RequestParam String clientName,
            @RequestParam String testimonialText,
            @RequestParam(required = false) String clientTitle,
            @RequestParam(required = false) String clientCompany,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String testimonialDate,
            @RequestParam(required = false) Boolean isFeatured,
            @RequestParam(required = false) Boolean isApproved,
            @RequestParam(required = false) Integer sortOrder,
            @RequestParam(required = false) String clientWebsite,
            @RequestParam(required = false) String clientLinkedin,
            @RequestParam(required = false) String videoTestimonialUrl,
            @RequestParam(required = false) String audioTestimonialUrl,
            @RequestParam(required = false) MultipartFile clientImage) {

        try {
            Testimonial testimonial = new Testimonial();
            testimonial.setUserId(userId);
            testimonial.setClientName(clientName);
            testimonial.setTestimonialText(testimonialText);
            testimonial.setClientTitle(clientTitle);
            testimonial.setClientCompany(clientCompany);
            testimonial.setRating(rating);
            testimonial.setProjectName(projectName);
            testimonial.setIsFeatured(isFeatured != null ? isFeatured : false);
            testimonial.setIsApproved(isApproved != null ? isApproved : true);
            testimonial.setSortOrder(sortOrder != null ? sortOrder : 0);
            testimonial.setClientWebsite(clientWebsite);
            testimonial.setClientLinkedin(clientLinkedin);
            testimonial.setVideoTestimonialUrl(videoTestimonialUrl);
            testimonial.setAudioTestimonialUrl(audioTestimonialUrl);

            // Parse testimonial date
            if (testimonialDate != null && !testimonialDate.trim().isEmpty()) {
                testimonial.setTestimonialDate(LocalDate.parse(testimonialDate));
            }

            // Save testimonial first
            Testimonial savedTestimonial = testimonialService.saveTestimonial(testimonial);

            // Handle client image upload
            if (clientImage != null && !clientImage.isEmpty()) {
                String imageUrl = cloudinaryService.uploadTestimonialImage(clientImage, savedTestimonial.getId());
                savedTestimonial.setClientImageUrl(imageUrl);
                savedTestimonial = testimonialService.saveTestimonial(savedTestimonial);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedTestimonial);

        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload client image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Testimonial> updateTestimonial(@PathVariable UUID id, @RequestBody Testimonial testimonialDetails) {
        try {
            Testimonial updatedTestimonial = testimonialService.updateTestimonial(id, testimonialDetails);
            return ResponseEntity.ok(updatedTestimonial);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestimonial(@PathVariable UUID id) {
        try {
            Optional<Testimonial> testimonialOpt = testimonialService.findById(id);
            if (testimonialOpt.isPresent()) {
                Testimonial testimonial = testimonialOpt.get();

                // Delete client image from Cloudinary
                if (testimonial.getClientImageUrl() != null) {
                    cloudinaryService.deleteImage(testimonial.getClientImageUrl());
                }

                if (testimonialService.deleteTestimonial(id)) {
                    return ResponseEntity.noContent().build();
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTestimonialCount() {
        long count = testimonialService.getTestimonialsCount();
        return ResponseEntity.ok(count);
    }
}
