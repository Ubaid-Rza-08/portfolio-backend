package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    // Find user by ID
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    // Find user by name (exact match)
    public Optional<User> findByName(String name) {
        return userRepository.findByNameIgnoreCase(name);
    }

    // Find users by name containing (partial match)
    public List<User> findByNameContaining(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    // Find user by title (exact match)
    public Optional<User> findByTitle(String title) {
        return userRepository.findByTitleIgnoreCase(title);
    }

    // Find users by title containing (partial match)
    public List<User> findByTitleContaining(String title) {
        return userRepository.findByTitleContainingIgnoreCase(title);
    }

    // Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Find users by location
    public List<User> findByLocation(String location) {
        return userRepository.findByLocation(location);
    }

    // Delete user by ID
    public boolean deleteUser(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Update user
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

                    // Update images if provided
                    if (userDetails.getProfileImage() != null) {
                        user.setProfileImage(userDetails.getProfileImage());
                    }
                    if (userDetails.getImage1() != null) {
                        user.setImage1(userDetails.getImage1());
                    }
                    if (userDetails.getImage2() != null) {
                        user.setImage2(userDetails.getImage2());
                    }
                    if (userDetails.getImage3() != null) {
                        user.setImage3(userDetails.getImage3());
                    }
                    if (userDetails.getImage4() != null) {
                        user.setImage4(userDetails.getImage4());
                    }

                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Helper method to convert MultipartFile to byte array
    public byte[] convertToByteArray(MultipartFile file) throws IOException {
        return file.getBytes();
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