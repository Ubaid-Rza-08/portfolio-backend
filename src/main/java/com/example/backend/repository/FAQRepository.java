package com.example.backend.repository;

import com.example.backend.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, UUID> {
    List<FAQ> findByUserIdAndIsPublishedTrueOrderBySortOrder(UUID userId);
    List<FAQ> findByUserIdAndIsFeaturedTrue(UUID userId);
    List<FAQ> findByCategory(String category);

    @Query("SELECT f FROM FAQ f WHERE f.userId = :userId AND (LOWER(f.question) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(f.answer) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<FAQ> searchByUserIdAndQuery(@Param("userId") UUID userId, @Param("query") String query);
}
