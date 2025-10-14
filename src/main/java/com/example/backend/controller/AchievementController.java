package com.example.backend.controller;

import com.example.backend.entity.Achievement;
import com.example.backend.service.AchievementService;
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
@RequestMapping("/api/achievements")
@CrossOrigin(origins = "*")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Achievement>> getAchievementsByUserId(@PathVariable UUID userId) {
        List<Achievement> achievements = achievementService.findByUserIdOrderByDate(userId);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<Achievement>> getActiveAchievements(@PathVariable UUID userId) {
        List<Achievement> achievements = achievementService.findActiveByUserId(userId);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/user/{userId}/featured")
    public ResponseEntity<List<Achievement>> getFeaturedAchievements(@PathVariable UUID userId) {
        List<Achievement> achievements = achievementService.findFeaturedByUserId(userId);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Achievement> getAchievementById(@PathVariable UUID id) {
        Optional<Achievement> achievement = achievementService.findById(id);
        return achievement.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Achievement>> getAchievementsByCategory(@PathVariable String category) {
        List<Achievement> achievements = achievementService.findByCategory(category);
        return ResponseEntity.ok(achievements);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAchievement(
            @RequestParam UUID userId,
            @RequestParam String title,
            @RequestParam String issuer,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String dateEarned,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String certificateUrl,
            @RequestParam(required = false) String verificationUrl,
            @RequestParam(required = false) Boolean isFeatured,
            @RequestParam(required = false) Integer sortOrder,
            @RequestParam(required = false) String expiryDate,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String credentialId,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) String competencyLevel,
            @RequestParam(required = false) MultipartFile badgeImage) {

        try {
            Achievement achievement = new Achievement();
            achievement.setUserId(userId);
            achievement.setTitle(title);
            achievement.setIssuer(issuer);
            achievement.setDescription(description);
            achievement.setCategory(category);
            achievement.setCertificateUrl(certificateUrl);
            achievement.setVerificationUrl(verificationUrl);
            achievement.setIsFeatured(isFeatured != null ? isFeatured : false);
            achievement.setSortOrder(sortOrder != null ? sortOrder : 0);
            achievement.setIsActive(isActive != null ? isActive : true);
            achievement.setCredentialId(credentialId);
            achievement.setSkills(skills);
            achievement.setCompetencyLevel(competencyLevel);

            // Parse dates
            if (dateEarned != null && !dateEarned.trim().isEmpty()) {
                achievement.setDateEarned(LocalDate.parse(dateEarned));
            }
            if (expiryDate != null && !expiryDate.trim().isEmpty()) {
                achievement.setExpiryDate(LocalDate.parse(expiryDate));
            }

            // Save achievement first
            Achievement savedAchievement = achievementService.saveAchievement(achievement);

            // Handle badge image upload
            if (badgeImage != null && !badgeImage.isEmpty()) {
                String imageUrl = cloudinaryService.uploadAchievementBadge(badgeImage, savedAchievement.getId());
                savedAchievement.setBadgeImageUrl(imageUrl);
                savedAchievement = achievementService.saveAchievement(savedAchievement);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedAchievement);

        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload badge image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Achievement> updateAchievement(@PathVariable UUID id, @RequestBody Achievement achievementDetails) {
        try {
            Achievement updatedAchievement = achievementService.updateAchievement(id, achievementDetails);
            return ResponseEntity.ok(updatedAchievement);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable UUID id) {
        try {
            Optional<Achievement> achievementOpt = achievementService.findById(id);
            if (achievementOpt.isPresent()) {
                Achievement achievement = achievementOpt.get();

                // Delete badge image from Cloudinary
                if (achievement.getBadgeImageUrl() != null) {
                    cloudinaryService.deleteImage(achievement.getBadgeImageUrl());
                }

                if (achievementService.deleteAchievement(id)) {
                    return ResponseEntity.noContent().build();
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
