package com.example.backend.repository;

import com.example.backend.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlogRepository extends JpaRepository<Blog, UUID> {
    List<Blog> findByUserIdAndStatusOrderByPublishedAtDesc(UUID userId, String status);
    List<Blog> findByUserIdAndIsFeaturedTrue(UUID userId);
    List<Blog> findByCategory(String category);
    Optional<Blog> findBySlug(String slug);

    @Query("SELECT b FROM Blog b WHERE b.userId = :userId AND LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Blog> findByUserIdAndTitleContainingIgnoreCase(@Param("userId") UUID userId, @Param("title") String title);
}
