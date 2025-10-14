package com.example.backend.repository;

import com.example.backend.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
    List<Portfolio> findByUserIdOrderBySortOrder(UUID userId);
    List<Portfolio> findByUserIdAndIsFeaturedTrue(UUID userId);
    List<Portfolio> findByCategory(String category);
    List<Portfolio> findByStatus(String status);

    @Query("SELECT p FROM Portfolio p WHERE p.userId = :userId AND LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Portfolio> findByUserIdAndTitleContainingIgnoreCase(@Param("userId") UUID userId, @Param("title") String title);
}
