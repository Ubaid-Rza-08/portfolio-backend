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

    // Find user by name (case-insensitive)
    @Query("SELECT u FROM User u WHERE LOWER(u.name) = LOWER(:name)")
    Optional<User> findByNameIgnoreCase(@Param("name") String name);

    // Find users by name containing (partial match, case-insensitive)
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameContainingIgnoreCase(@Param("name") String name);

    // Find user by title (case-insensitive)
    @Query("SELECT u FROM User u WHERE LOWER(u.title) = LOWER(:title)")
    Optional<User> findByTitleIgnoreCase(@Param("title") String title);

    // Find users by title containing (partial match, case-insensitive)
    @Query("SELECT u FROM User u WHERE LOWER(u.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<User> findByTitleContainingIgnoreCase(@Param("title") String title);

    // Find users by email
    Optional<User> findByEmail(String email);

    // Find users by location
    List<User> findByLocation(String location);

    // Find users by experience year range
    @Query("SELECT u FROM User u WHERE u.experienceYear >= :minExp AND u.experienceYear <= :maxExp")
    List<User> findByExperienceYearBetween(@Param("minExp") Integer minExp, @Param("maxExp") Integer maxExp);
}