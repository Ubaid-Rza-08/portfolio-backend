package com.example.backend.service;

import com.example.backend.entity.Achievement;
import com.example.backend.repository.AchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    public Achievement saveAchievement(Achievement achievement) {
        return achievementRepository.save(achievement);
    }

    public List<Achievement> findActiveByUserId(UUID userId) {
        return achievementRepository.findByUserIdAndIsActiveTrueOrderBySortOrder(userId);
    }

    public List<Achievement> findFeaturedByUserId(UUID userId) {
        return achievementRepository.findByUserIdAndIsFeaturedTrue(userId);
    }

    public List<Achievement> findByUserIdOrderByDate(UUID userId) {
        return achievementRepository.findByUserIdOrderByDateEarnedDesc(userId);
    }

    public Optional<Achievement> findById(UUID id) {
        return achievementRepository.findById(id);
    }

    public List<Achievement> findByCategory(String category) {
        return achievementRepository.findByCategory(category);
    }

    public Achievement updateAchievement(UUID id, Achievement details) {
        return achievementRepository.findById(id)
                .map(achievement -> {
                    achievement.setTitle(details.getTitle());
                    achievement.setDescription(details.getDescription());
                    achievement.setIssuer(details.getIssuer());
                    achievement.setDateEarned(details.getDateEarned());
                    achievement.setCategory(details.getCategory());
                    achievement.setCertificateUrl(details.getCertificateUrl());
                    achievement.setVerificationUrl(details.getVerificationUrl());
                    achievement.setIsFeatured(details.getIsFeatured());
                    achievement.setSortOrder(details.getSortOrder());
                    achievement.setExpiryDate(details.getExpiryDate());
                    achievement.setIsActive(details.getIsActive());
                    achievement.setCredentialId(details.getCredentialId());
                    achievement.setSkills(details.getSkills());
                    achievement.setCompetencyLevel(details.getCompetencyLevel());
                    if (details.getBadgeImageUrl() != null) {
                        achievement.setBadgeImageUrl(details.getBadgeImageUrl());
                    }
                    return achievementRepository.save(achievement);
                })
                .orElseThrow(() -> new RuntimeException("Achievement not found with id: " + id));
    }

    public boolean deleteAchievement(UUID id) {
        if (achievementRepository.existsById(id)) {
            achievementRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
