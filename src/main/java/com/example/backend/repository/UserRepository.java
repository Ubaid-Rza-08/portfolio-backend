package com.example.backend.repository;

import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Find user by name (case-insensitive exact match)
    @Query("SELECT u FROM User u WHERE LOWER(u.name) = LOWER(:name)")
    Optional<User> findByNameIgnoreCase(@Param("name") String name);

    // Find users by name containing (partial match, case-insensitive)
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameContainingIgnoreCase(@Param("name") String name);

    // Find user by title (case-insensitive exact match)
    @Query("SELECT u FROM User u WHERE LOWER(u.title) = LOWER(:title)")
    Optional<User> findByTitleIgnoreCase(@Param("title") String title);

    // Find users by title containing (partial match, case-insensitive)
    @Query("SELECT u FROM User u WHERE LOWER(u.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<User> findByTitleContainingIgnoreCase(@Param("title") String title);

    // Find users by description containing (partial match, case-insensitive)
    @Query("SELECT u FROM User u WHERE LOWER(u.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    List<User> findByDescriptionContainingIgnoreCase(@Param("description") String description);

    // Search users by keyword in name, title, or description
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchByKeyword(@Param("keyword") String keyword);

    // Find users by name or title containing keyword
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> findByNameOrTitleContaining(@Param("keyword") String keyword);

    // Check if user exists by name (case-insensitive)
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.name) = LOWER(:name)")
    boolean existsByNameIgnoreCase(@Param("name") String name);

    // Check if user exists by title (case-insensitive)
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.title) = LOWER(:title)")
    boolean existsByTitleIgnoreCase(@Param("title") String title);

    // Find users ordered by name
    @Query("SELECT u FROM User u ORDER BY u.name ASC")
    List<User> findAllOrderByName();

    // Find users ordered by title
    @Query("SELECT u FROM User u ORDER BY u.title ASC")
    List<User> findAllOrderByTitle();

    // Find users with empty or null fields for data validation
    @Query("SELECT u FROM User u WHERE u.name IS NULL OR u.name = ''")
    List<User> findUsersWithEmptyName();

    @Query("SELECT u FROM User u WHERE u.title IS NULL OR u.title = ''")
    List<User> findUsersWithEmptyTitle();

    @Query("SELECT u FROM User u WHERE u.description IS NULL OR u.description = ''")
    List<User> findUsersWithEmptyDescription();

    // Count users by partial name match
    @Query("SELECT COUNT(u) FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    long countByNameContaining(@Param("name") String name);

    // Count users by partial title match
    @Query("SELECT COUNT(u) FROM User u WHERE LOWER(u.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    long countByTitleContaining(@Param("title") String title);

    // Get distinct titles (useful for filtering/categorization)
    @Query("SELECT DISTINCT u.title FROM User u WHERE u.title IS NOT NULL ORDER BY u.title")
    List<String> findDistinctTitles();

    // Find users with similar names (useful for duplicate detection)
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) AND u.id != :excludeId")
    List<User> findSimilarNames(@Param("name") String name, @Param("excludeId") UUID excludeId);
}