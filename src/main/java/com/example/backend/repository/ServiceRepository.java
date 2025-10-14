package com.example.backend.repository;

import com.example.backend.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {
    List<Service> findByUserIdAndIsActiveTrue(UUID userId);
    List<Service> findByUserIdOrderBySortOrder(UUID userId);
    List<Service> findByCategory(String category);

    @Query("SELECT s FROM Service s WHERE s.userId = :userId AND LOWER(s.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Service> findByUserIdAndTitleContainingIgnoreCase(@Param("userId") UUID userId, @Param("title") String title);
}
