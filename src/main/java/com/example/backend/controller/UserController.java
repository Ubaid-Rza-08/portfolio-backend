package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Find user by ID
    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Find user by name (exact match)
    @GetMapping("/name/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable String name) {
        Optional<User> user = userService.findByName(name);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Find users by name containing (partial match)
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<User>> getUsersByNameContaining(@PathVariable String name) {
        List<User> users = userService.findByNameContaining(name);
        return ResponseEntity.ok(users);
    }

    // Find user by title (exact match)
    @GetMapping("/title/{title}")
    public ResponseEntity<User> getUserByTitle(@PathVariable String title) {
        Optional<User> user = userService.findByTitle(title);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Find users by title containing (partial match)
    @GetMapping("/search/title/{title}")
    public ResponseEntity<List<User>> getUsersByTitleContaining(@PathVariable String title) {
        List<User> users = userService.findByTitleContaining(title);
        return ResponseEntity.ok(users);
    }

    // Find user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Find users by location
    @GetMapping("/location/{location}")
    public ResponseEntity<List<User>> getUsersByLocation(@PathVariable String location) {
        List<User> users = userService.findByLocation(location);
        return ResponseEntity.ok(users);
    }

    // Create new user with form data and file uploads
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> createUser(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile profileImage,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer experienceYear,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String faceBookUrl,
            @RequestParam(required = false) String linkedInUrl,
            @RequestParam(required = false) String twitterUrl,
            @RequestParam(required = false) String instaUrl,
            @RequestParam(required = false) String dribbleUrl,
            @RequestParam(required = false) String otherUrl,
            @RequestParam(required = false) String day,
            @RequestParam(required = false) String openTime,
            @RequestParam(required = false) String closeTime,
            @RequestParam(required = false) String serviceTitle,
            @RequestParam(required = false) String serviceDescription,
            @RequestParam(required = false) MultipartFile image1,
            @RequestParam(required = false) String description1,
            @RequestParam(required = false) MultipartFile image2,
            @RequestParam(required = false) String description2,
            @RequestParam(required = false) MultipartFile image3,
            @RequestParam(required = false) String description3,
            @RequestParam(required = false) MultipartFile image4,
            @RequestParam(required = false) String description4
    ) {
        try {
            User user = new User();
            user.setName(name);
            user.setTitle(title);
            user.setDescription(description);
            user.setLocation(location);
            user.setExperienceYear(experienceYear);
            user.setEmail(email);
            user.setPhone(phone);
            user.setFaceBookUrl(faceBookUrl);
            user.setLinkedInUrl(linkedInUrl);
            user.setTwitterUrl(twitterUrl);
            user.setInstaUrl(instaUrl);
            user.setDribbleUrl(dribbleUrl);
            user.setOtherUrl(otherUrl);
            user.setDay(day);
            user.setServiceTitle(serviceTitle);
            user.setServiceDescription(serviceDescription);
            user.setDescription1(description1);
            user.setDescription2(description2);
            user.setDescription3(description3);
            user.setDescription4(description4);

            // Parse time fields (handle both 12-hour and 24-hour formats)
            if (openTime != null && !openTime.trim().isEmpty()) {
                user.setOpenTime(parseTime(openTime));
            }
            if (closeTime != null && !closeTime.trim().isEmpty()) {
                user.setCloseTime(parseTime(closeTime));
            }

            // Handle image uploads
            if (profileImage != null && !profileImage.isEmpty()) {
                user.setProfileImage(userService.convertToByteArray(profileImage));
            }
            if (image1 != null && !image1.isEmpty()) {
                user.setImage1(userService.convertToByteArray(image1));
            }
            if (image2 != null && !image2.isEmpty()) {
                user.setImage2(userService.convertToByteArray(image2));
            }
            if (image3 != null && !image3.isEmpty()) {
                user.setImage3(userService.convertToByteArray(image3));
            }
            if (image4 != null && !image4.isEmpty()) {
                user.setImage4(userService.convertToByteArray(image4));
            }

            User savedUser = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create user with JSON (for testing without file uploads)
    @PostMapping("/json")
    public ResponseEntity<User> createUserJson(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get user count
    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        long count = userService.getUsersCount();
        return ResponseEntity.ok(count);
    }

    // Check if user exists
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> userExists(@PathVariable UUID id) {
        boolean exists = userService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    // Get profile image
    @GetMapping("/{id}/profile-image")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable UUID id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent() && user.get().getProfileImage() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(user.get().getProfileImage());
        }
        return ResponseEntity.notFound().build();
    }

    // Helper method to parse time in multiple formats
    private LocalTime parseTime(String timeString) {
        try {
            // Remove spaces and convert to uppercase for consistent parsing
            timeString = timeString.trim().toUpperCase();

            // Handle AM/PM format
            if (timeString.contains("AM") || timeString.contains("PM")) {
                // Remove AM/PM and parse
                String cleanTime = timeString.replaceAll("[AP]M", "").trim();
                boolean isPM = timeString.contains("PM");

                // Parse hour and minute
                String[] parts = cleanTime.split(":");
                int hour = Integer.parseInt(parts[0]);
                int minute = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;

                // Convert to 24-hour format
                if (isPM && hour != 12) {
                    hour += 12;
                } else if (!isPM && hour == 12) {
                    hour = 0;
                }

                return LocalTime.of(hour, minute);
            } else {
                // Try 24-hour format (HH:mm)
                if (timeString.contains(":")) {
                    return LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
                } else {
                    // Try HH format
                    int hour = Integer.parseInt(timeString);
                    return LocalTime.of(hour, 0);
                }
            }
        } catch (Exception e) {
            // If all parsing fails, default to current time or throw exception
            throw new IllegalArgumentException("Invalid time format: " + timeString + ". Supported formats: HH:mm, H:mmAM/PM, HH:mm");
        }
    }

    // Get specific image by number
    @GetMapping("/{id}/image/{imageNumber}")
    public ResponseEntity<byte[]> getImage(@PathVariable UUID id, @PathVariable int imageNumber) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            byte[] image = null;
            switch (imageNumber) {
                case 1:
                    image = user.get().getImage1();
                    break;
                case 2:
                    image = user.get().getImage2();
                    break;
                case 3:
                    image = user.get().getImage3();
                    break;
                case 4:
                    image = user.get().getImage4();
                    break;
            }
            if (image != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(image);
            }
        }
        return ResponseEntity.notFound().build();
    }
}