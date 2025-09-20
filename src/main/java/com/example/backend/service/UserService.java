package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Arrays;
import java.util.Collections;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Create or update user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Find user by ID - returns list for consistency
    public List<User> findById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(Arrays::asList).orElse(Collections.emptyList());
    }

    // Find user by name (exact match) - returns list for consistency
    public List<User> findByName(String name) {
        Optional<User> user = userRepository.findByNameIgnoreCase(name);
        return user.map(Arrays::asList).orElse(Collections.emptyList());
    }

    // Find users by name containing (partial match)
    public List<User> findByNameContaining(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    // Find user by title (exact match) - returns list for consistency
    public List<User> findByTitle(String title) {
        Optional<User> user = userRepository.findByTitleIgnoreCase(title);
        return user.map(Arrays::asList).orElse(Collections.emptyList());
    }

    // Find users by title containing (partial match)
    public List<User> findByTitleContaining(String title) {
        return userRepository.findByTitleContainingIgnoreCase(title);
    }

    // Find user by email - returns list for consistency
    public List<User> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(Arrays::asList).orElse(Collections.emptyList());
    }

    // Find users by location
    public List<User> findByLocation(String location) {
        return userRepository.findByLocation(location);
    }

    // Find users by experience year range
    public List<User> findByExperienceYearBetween(Integer minExp, Integer maxExp) {
        return userRepository.findByExperienceYearBetween(minExp, maxExp);
    }

    // Helper methods for backward compatibility (used by controller for updates/deletes)
    public Optional<User> findByIdOptional(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByNameOptional(String name) {
        return userRepository.findByNameIgnoreCase(name);
    }

    public Optional<User> findByTitleOptional(String title) {
        return userRepository.findByTitleIgnoreCase(title);
    }

    public Optional<User> findByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }

    // Delete user by ID
    public boolean deleteUser(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Update user (with Cloudinary URLs)
    public User updateUser(UUID id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(userDetails.getName());
                    user.setTitle(userDetails.getTitle());
                    user.setDescription(userDetails.getDescription());
                    user.setLocation(userDetails.getLocation());
                    user.setExperienceYear(userDetails.getExperienceYear());
                    user.setEmail(userDetails.getEmail());
                    user.setPhone(userDetails.getPhone());
                    user.setFaceBookUrl(userDetails.getFaceBookUrl());
                    user.setLinkedInUrl(userDetails.getLinkedInUrl());
                    user.setTwitterUrl(userDetails.getTwitterUrl());
                    user.setInstaUrl(userDetails.getInstaUrl());
                    user.setDribbleUrl(userDetails.getDribbleUrl());
                    user.setOtherUrl(userDetails.getOtherUrl());
                    user.setDay(userDetails.getDay());
                    user.setOpenTime(userDetails.getOpenTime());
                    user.setCloseTime(userDetails.getCloseTime());
                    user.setServiceTitle(userDetails.getServiceTitle());
                    user.setServiceDescription(userDetails.getServiceDescription());
                    user.setDescription1(userDetails.getDescription1());
                    user.setDescription2(userDetails.getDescription2());
                    user.setDescription3(userDetails.getDescription3());
                    user.setDescription4(userDetails.getDescription4());

                    // Update image URLs if provided (not null or empty)
                    if (userDetails.getProfileImageUrl() != null && !userDetails.getProfileImageUrl().isEmpty()) {
                        user.setProfileImageUrl(userDetails.getProfileImageUrl());
                    }
                    if (userDetails.getImage1Url() != null && !userDetails.getImage1Url().isEmpty()) {
                        user.setImage1Url(userDetails.getImage1Url());
                    }
                    if (userDetails.getImage2Url() != null && !userDetails.getImage2Url().isEmpty()) {
                        user.setImage2Url(userDetails.getImage2Url());
                    }
                    if (userDetails.getImage3Url() != null && !userDetails.getImage3Url().isEmpty()) {
                        user.setImage3Url(userDetails.getImage3Url());
                    }
                    if (userDetails.getImage4Url() != null && !userDetails.getImage4Url().isEmpty()) {
                        user.setImage4Url(userDetails.getImage4Url());
                    }

                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Check if user exists by ID
    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    // Get users count
    public long getUsersCount() {
        return userRepository.count();
    }
}