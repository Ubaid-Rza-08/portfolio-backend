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

    // Delete user by ID
    public boolean deleteUser(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Update user - only with available fields
    public User updateUser(UUID id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    if (userDetails.getName() != null) {
                        user.setName(userDetails.getName());
                    }
                    if (userDetails.getTitle() != null) {
                        user.setTitle(userDetails.getTitle());
                    }
                    if (userDetails.getDescription() != null) {
                        user.setDescription(userDetails.getDescription());
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