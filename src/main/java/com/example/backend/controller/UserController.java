package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import com.example.backend.service.CloudinaryService;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Find user by ID - returns list for consistency
    @GetMapping("/id/{id}")
    public ResponseEntity<List<User>> getUserById(@PathVariable UUID id) {
        List<User> users = userService.findById(id);
        return ResponseEntity.ok(users);
    }

    // Find user by name (exact match) - returns list for consistency
    @GetMapping("/name/{name}")
    public ResponseEntity<List<User>> getUserByName(@PathVariable String name) {
        List<User> users = userService.findByNameContaining(name);
        return ResponseEntity.ok(users);
    }

//    // Find users by name containing (partial match)
//    @GetMapping("/search/name/{name}")
//    public ResponseEntity<List<User>> getUsersByNameContaining(@PathVariable String name) {
//        List<User> users = userService.findByNameContaining(name);
//        return ResponseEntity.ok(users);
//    }

    // Find user by title (exact match) - returns list for consistency
    @GetMapping("/title/{title}")
    public ResponseEntity<List<User>> getUserByTitle(@PathVariable String title) {
        List<User> users = userService.findByTitleContaining(title);
        return ResponseEntity.ok(users);
    }

//    // Find users by title containing (partial match)
//    @GetMapping("/search/title/{title}")
//    public ResponseEntity<List<User>> getUsersByTitleContaining(@PathVariable String title) {
//        List<User> users = userService.findByTitleContaining(title);
//        return ResponseEntity.ok(users);
//    }

    // Find user by email - returns list for consistency
    @GetMapping("/email/{email}")
    public ResponseEntity<List<User>> getUserByEmail(@PathVariable String email) {
        List<User> users = userService.findByEmail(email);
        return ResponseEntity.ok(users);
    }

    // Find users by location
    @GetMapping("/location/{location}")
    public ResponseEntity<List<User>> getUsersByLocation(@PathVariable String location) {
        List<User> users = userService.findByLocation(location);
        return ResponseEntity.ok(users);
    }

    // Find users by experience year range
    @GetMapping("/experience/{minExp}/{maxExp}")
    public ResponseEntity<List<User>> getUsersByExperienceRange(@PathVariable Integer minExp, @PathVariable Integer maxExp) {
        List<User> users = userService.findByExperienceYearBetween(minExp, maxExp);
        return ResponseEntity.ok(users);
    }
    // Helper method to parse time in multiple formats
    private LocalTime parseTime(String timeString) {
        if (timeString == null || timeString.trim().isEmpty()) {
            return null; // Return null instead of throwing exception for empty strings
        }

        try {
            // Remove spaces and convert to uppercase for consistent parsing
            timeString = timeString.trim().toUpperCase();

            // Handle AM/PM format
            if (timeString.contains("AM") || timeString.contains("PM")) {
                boolean isPM = timeString.contains("PM");

                // Remove AM/PM and clean up
                String cleanTime = timeString.replaceAll("\\s*[AP]M\\s*", "").trim();

                // Handle various formats like "9", "9:30", "09", "09:30"
                String[] parts;
                if (cleanTime.contains(":")) {
                    parts = cleanTime.split(":");
                } else {
                    // Handle cases like "9AM", "12PM" (no colon)
                    parts = new String[]{cleanTime, "0"};
                }

                if (parts.length < 1 || parts.length > 2) {
                    throw new IllegalArgumentException("Invalid time format: " + timeString);
                }

                int hour = Integer.parseInt(parts[0].trim());
                int minute = parts.length > 1 ? Integer.parseInt(parts[1].trim()) : 0;

                // Validate hour and minute ranges
                if (hour < 1 || hour > 12) {
                    throw new IllegalArgumentException("Invalid hour in 12-hour format: " + hour);
                }
                if (minute < 0 || minute > 59) {
                    throw new IllegalArgumentException("Invalid minute: " + minute);
                }

                // Convert to 24-hour format
                if (isPM && hour != 12) {
                    hour += 12;
                } else if (!isPM && hour == 12) {
                    hour = 0;
                }

                return LocalTime.of(hour, minute);

            } else {
                // Handle 24-hour format
                if (timeString.contains(":")) {
                    // Format like "HH:mm" or "H:mm"
                    String[] parts = timeString.split(":");
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Invalid time format: " + timeString);
                    }

                    int hour = Integer.parseInt(parts[0].trim());
                    int minute = Integer.parseInt(parts[1].trim());

                    // Validate ranges
                    if (hour < 0 || hour > 23) {
                        throw new IllegalArgumentException("Invalid hour in 24-hour format: " + hour);
                    }
                    if (minute < 0 || minute > 59) {
                        throw new IllegalArgumentException("Invalid minute: " + minute);
                    }

                    return LocalTime.of(hour, minute);
                } else {
                    // Format like "HH" or "H" (hour only)
                    int hour = Integer.parseInt(timeString.trim());

                    // Validate hour range
                    if (hour < 0 || hour > 23) {
                        throw new IllegalArgumentException("Invalid hour in 24-hour format: " + hour);
                    }

                    return LocalTime.of(hour, 0);
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid time format - contains non-numeric characters: " + timeString, e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid time format: " + timeString +
                    ". Supported formats: HH:mm (24-hour), H:mmAM/PM (12-hour), HH (hour only)", e);
        }
    }

    // Create new user with form data and file uploads
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createUser(
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

            // Save user first to get the ID
            User savedUser = userService.saveUser(user);

            // Handle image uploads to Cloudinary
            if (profileImage != null && !profileImage.isEmpty()) {
                String profileImageUrl = cloudinaryService.uploadProfileImage(profileImage, savedUser.getId());
                savedUser.setProfileImageUrl(profileImageUrl);
            }
            if (image1 != null && !image1.isEmpty()) {
                String image1Url = cloudinaryService.uploadGalleryImage(image1, savedUser.getId(), 1);
                savedUser.setImage1Url(image1Url);
            }
            if (image2 != null && !image2.isEmpty()) {
                String image2Url = cloudinaryService.uploadGalleryImage(image2, savedUser.getId(), 2);
                savedUser.setImage2Url(image2Url);
            }
            if (image3 != null && !image3.isEmpty()) {
                String image3Url = cloudinaryService.uploadGalleryImage(image3, savedUser.getId(), 3);
                savedUser.setImage3Url(image3Url);
            }
            if (image4 != null && !image4.isEmpty()) {
                String image4Url = cloudinaryService.uploadGalleryImage(image4, savedUser.getId(), 4);
                savedUser.setImage4Url(image4Url);
            }

            // Update user with image URLs
            User finalUser = userService.saveUser(savedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(finalUser);

        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload images: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
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

    // Update user with form data (including images)
    @PutMapping(value = "/{id}/form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserWithForm(
            @PathVariable UUID id,
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
            Optional<User> existingUserOpt = userService.findByIdOptional(id);
            if (existingUserOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = existingUserOpt.get();

            // Update fields if provided
            if (name != null) user.setName(name);
            if (title != null) user.setTitle(title);
            if (description != null) user.setDescription(description);
            if (location != null) user.setLocation(location);
            if (experienceYear != null) user.setExperienceYear(experienceYear);
            if (email != null) user.setEmail(email);
            if (phone != null) user.setPhone(phone);
            if (faceBookUrl != null) user.setFaceBookUrl(faceBookUrl);
            if (linkedInUrl != null) user.setLinkedInUrl(linkedInUrl);
            if (twitterUrl != null) user.setTwitterUrl(twitterUrl);
            if (instaUrl != null) user.setInstaUrl(instaUrl);
            if (dribbleUrl != null) user.setDribbleUrl(dribbleUrl);
            if (otherUrl != null) user.setOtherUrl(otherUrl);
            if (day != null) user.setDay(day);
            if (serviceTitle != null) user.setServiceTitle(serviceTitle);
            if (serviceDescription != null) user.setServiceDescription(serviceDescription);
            if (description1 != null) user.setDescription1(description1);
            if (description2 != null) user.setDescription2(description2);
            if (description3 != null) user.setDescription3(description3);
            if (description4 != null) user.setDescription4(description4);

            // Parse time fields
            if (openTime != null && !openTime.trim().isEmpty()) {
                user.setOpenTime(parseTime(openTime));
            }
            if (closeTime != null && !closeTime.trim().isEmpty()) {
                user.setCloseTime(parseTime(closeTime));
            }

            // Handle image uploads and replacements
            if (profileImage != null && !profileImage.isEmpty()) {
                // Delete old image if exists
                if (user.getProfileImageUrl() != null) {
                    cloudinaryService.deleteImage(user.getProfileImageUrl());
                }
                String profileImageUrl = cloudinaryService.uploadProfileImage(profileImage, user.getId());
                user.setProfileImageUrl(profileImageUrl);
            }

            if (image1 != null && !image1.isEmpty()) {
                if (user.getImage1Url() != null) {
                    cloudinaryService.deleteImage(user.getImage1Url());
                }
                String image1Url = cloudinaryService.uploadGalleryImage(image1, user.getId(), 1);
                user.setImage1Url(image1Url);
            }

            if (image2 != null && !image2.isEmpty()) {
                if (user.getImage2Url() != null) {
                    cloudinaryService.deleteImage(user.getImage2Url());
                }
                String image2Url = cloudinaryService.uploadGalleryImage(image2, user.getId(), 2);
                user.setImage2Url(image2Url);
            }

            if (image3 != null && !image3.isEmpty()) {
                if (user.getImage3Url() != null) {
                    cloudinaryService.deleteImage(user.getImage3Url());
                }
                String image3Url = cloudinaryService.uploadGalleryImage(image3, user.getId(), 3);
                user.setImage3Url(image3Url);
            }

            if (image4 != null && !image4.isEmpty()) {
                if (user.getImage4Url() != null) {
                    cloudinaryService.deleteImage(user.getImage4Url());
                }
                String image4Url = cloudinaryService.uploadGalleryImage(image4, user.getId(), 4);
                user.setImage4Url(image4Url);
            }

            User updatedUser = userService.saveUser(user);
            return ResponseEntity.ok(updatedUser);

        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload images: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        try {
            Optional<User> userOpt = userService.findByIdOptional(id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();

                // Delete all images from Cloudinary before deleting user
                if (user.getProfileImageUrl() != null) {
                    cloudinaryService.deleteImage(user.getProfileImageUrl());
                }
                if (user.getImage1Url() != null) {
                    cloudinaryService.deleteImage(user.getImage1Url());
                }
                if (user.getImage2Url() != null) {
                    cloudinaryService.deleteImage(user.getImage2Url());
                }
                if (user.getImage3Url() != null) {
                    cloudinaryService.deleteImage(user.getImage3Url());
                }
                if (user.getImage4Url() != null) {
                    cloudinaryService.deleteImage(user.getImage4Url());
                }

                // Delete user from database
                if (userService.deleteUser(id)) {
                    return ResponseEntity.noContent().build();
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

    // Get profile image URL with different sizes
    @GetMapping("/{id}/profile-image")
    public ResponseEntity<Map<String, String>> getProfileImageUrls(@PathVariable UUID id) {
        Optional<User> user = userService.findByIdOptional(id);
        if (user.isPresent() && user.get().getProfileImageUrl() != null) {
            String originalUrl = user.get().getProfileImageUrl();
            Map<String, String> imageUrls = new HashMap<>();
            imageUrls.put("original", originalUrl);
            imageUrls.put("thumbnail", cloudinaryService.getThumbnailUrl(originalUrl));
            imageUrls.put("medium", cloudinaryService.getMediumImageUrl(originalUrl));
            return ResponseEntity.ok(imageUrls);
        }
        return ResponseEntity.notFound().build();
    }

    // Get specific gallery image URLs with different sizes
    @GetMapping("/{id}/image/{imageNumber}")
    public ResponseEntity<Map<String, String>> getImageUrls(@PathVariable UUID id, @PathVariable int imageNumber) {
        Optional<User> user = userService.findByIdOptional(id);
        if (user.isPresent()) {
            String imageUrl = null;
            switch (imageNumber) {
                case 1:
                    imageUrl = user.get().getImage1Url();
                    break;
                case 2:
                    imageUrl = user.get().getImage2Url();
                    break;
                case 3:
                    imageUrl = user.get().getImage3Url();
                    break;
                case 4:
                    imageUrl = user.get().getImage4Url();
                    break;
            }
            if (imageUrl != null) {
                Map<String, String> imageUrls = new HashMap<>();
                imageUrls.put("original", imageUrl);
                imageUrls.put("thumbnail", cloudinaryService.getThumbnailUrl(imageUrl));
                imageUrls.put("medium", cloudinaryService.getMediumImageUrl(imageUrl));
                return ResponseEntity.ok(imageUrls);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // Upload single profile image
    @PostMapping("/{id}/profile-image")
    public ResponseEntity<?> uploadProfileImage(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        try {
            Optional<User> userOpt = userService.findByIdOptional(id);
            if (userOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOpt.get();

            // Delete old profile image if exists
            if (user.getProfileImageUrl() != null) {
                cloudinaryService.deleteImage(user.getProfileImageUrl());
            }

            // Upload new image
            String imageUrl = cloudinaryService.uploadProfileImage(file, user.getId());
            user.setProfileImageUrl(imageUrl);

            User updatedUser = userService.saveUser(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Profile image uploaded successfully");
            response.put("imageUrl", imageUrl);
            response.put("thumbnailUrl", cloudinaryService.getThumbnailUrl(imageUrl));

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Upload single gallery image
    @PostMapping("/{id}/gallery-image/{imageNumber}")
    public ResponseEntity<?> uploadGalleryImage(@PathVariable UUID id, @PathVariable int imageNumber, @RequestParam("file") MultipartFile file) {
        try {
            if (imageNumber < 1 || imageNumber > 4) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Image number must be between 1 and 4");
                return ResponseEntity.badRequest().body(error);
            }

            Optional<User> userOpt = userService.findByIdOptional(id);
            if (userOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOpt.get();

            // Delete old image if exists
            String oldImageUrl = null;
            switch (imageNumber) {
                case 1:
                    oldImageUrl = user.getImage1Url();
                    break;
                case 2:
                    oldImageUrl = user.getImage2Url();
                    break;
                case 3:
                    oldImageUrl = user.getImage3Url();
                    break;
                case 4:
                    oldImageUrl = user.getImage4Url();
                    break;
            }

            if (oldImageUrl != null) {
                cloudinaryService.deleteImage(oldImageUrl);
            }

            // Upload new image
            String imageUrl = cloudinaryService.uploadGalleryImage(file, user.getId(), imageNumber);

            // Update user entity
            switch (imageNumber) {
                case 1:
                    user.setImage1Url(imageUrl);
                    break;
                case 2:
                    user.setImage2Url(imageUrl);
                    break;
                case 3:
                    user.setImage3Url(imageUrl);
                    break;
                case 4:
                    user.setImage4Url(imageUrl);
                    break;
            }

            User updatedUser = userService.saveUser(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Gallery image " + imageNumber + " uploaded successfully");
            response.put("imageUrl", imageUrl);
            response.put("thumbnailUrl", cloudinaryService.getThumbnailUrl(imageUrl));

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}