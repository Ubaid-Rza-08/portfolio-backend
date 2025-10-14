package com.example.backend.repository;

import com.example.backend.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, UUID> {
    List<Achievement> findByUserIdAndIsActiveTrueOrderBySortOrder(UUID userId);
    List<Achievement> findByUserIdAndIsFeaturedTrue(UUID userId);
    List<Achievement> findByCategory(String category);
    List<Achievement> findByUserIdOrderByDateEarnedDesc(UUID userId);
}
