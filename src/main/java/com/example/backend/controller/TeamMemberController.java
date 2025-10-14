package com.example.backend.controller;

import com.example.backend.entity.TeamMember;
import com.example.backend.service.TeamMemberService;
import com.example.backend.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/team-members")
@CrossOrigin(origins = "*")
public class TeamMemberController {

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private CloudinaryService cloudinaryService;

    // Get all active team members for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TeamMember>> getTeamMembersByUserId(@PathVariable UUID userId) {
        try {
            List<TeamMember> teamMembers = teamMemberService.findActiveByUserId(userId);
            return ResponseEntity.ok(teamMembers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get team member by ID
    @GetMapping("/{id}")
    public ResponseEntity<TeamMember> getTeamMemberById(@PathVariable UUID id) {
        try {
            Optional<TeamMember> teamMember = teamMemberService.findById(id);
            return teamMember.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get team members by department
    @GetMapping("/department/{department}")
    public ResponseEntity<List<TeamMember>> getTeamMembersByDepartment(@PathVariable String department) {
        try {
            List<TeamMember> teamMembers = teamMemberService.findByDepartment(department);
            return ResponseEntity.ok(teamMembers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get team members by location
    @GetMapping("/location/{location}")
    public ResponseEntity<List<TeamMember>> getTeamMembersByLocation(@PathVariable String location) {
        try {
            List<TeamMember> teamMembers = teamMemberService.findByLocation(location);
            return ResponseEntity.ok(teamMembers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create new team member with form data and image upload
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createTeamMember(
            @RequestParam UUID userId,
            @RequestParam String name,
            @RequestParam String role,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) Integer experienceYears,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String linkedinUrl,
            @RequestParam(required = false) String twitterUrl,
            @RequestParam(required = false) String portfolioUrl,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Integer sortOrder,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String hourlyRate,
            @RequestParam(required = false) MultipartFile image) {

        try {
            TeamMember teamMember = new TeamMember();
            teamMember.setUserId(userId);
            teamMember.setName(name);
            teamMember.setRole(role);
            teamMember.setBio(bio);
            teamMember.setSkills(skills);
            teamMember.setExperienceYears(experienceYears);
            teamMember.setEmail(email);
            teamMember.setLinkedinUrl(linkedinUrl);
            teamMember.setTwitterUrl(twitterUrl);
            teamMember.setPortfolioUrl(portfolioUrl);
            teamMember.setIsActive(isActive != null ? isActive : true);
            teamMember.setSortOrder(sortOrder != null ? sortOrder : 0);
            teamMember.setDepartment(department);
            teamMember.setLocation(location);

            // Parse hourly rate
            if (hourlyRate != null && !hourlyRate.trim().isEmpty()) {
                try {
                    teamMember.setHourlyRate(new BigDecimal(hourlyRate));
                } catch (NumberFormatException e) {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Invalid hourly rate format");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                }
            }

            // Save team member first to get ID
            TeamMember savedTeamMember = teamMemberService.saveTeamMember(teamMember);

            // Handle image upload
            if (image != null && !image.isEmpty()) {
                String imageUrl = cloudinaryService.uploadTeamMemberImage(image, savedTeamMember.getId());
                savedTeamMember.setImageUrl(imageUrl);
                savedTeamMember = teamMemberService.saveTeamMember(savedTeamMember);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedTeamMember);

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

    // Create team member with JSON (for testing without file uploads)
    @PostMapping("/json")
    public ResponseEntity<TeamMember> createTeamMemberJson(@RequestBody TeamMember teamMember) {
        try {
            TeamMember savedTeamMember = teamMemberService.saveTeamMember(teamMember);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTeamMember);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update team member with JSON
    @PutMapping("/{id}")
    public ResponseEntity<TeamMember> updateTeamMember(@PathVariable UUID id, @RequestBody TeamMember teamMemberDetails) {
        try {
            TeamMember updatedTeamMember = teamMemberService.updateTeamMember(id, teamMemberDetails);
            return ResponseEntity.ok(updatedTeamMember);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update team member with form data (including image)
    @PutMapping(value = "/{id}/form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateTeamMemberWithForm(
            @PathVariable UUID id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) Integer experienceYears,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String linkedinUrl,
            @RequestParam(required = false) String twitterUrl,
            @RequestParam(required = false) String portfolioUrl,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Integer sortOrder,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String hourlyRate,
            @RequestParam(required = false) MultipartFile image) {

        try {
            Optional<TeamMember> existingTeamMemberOpt = teamMemberService.findById(id);
            if (existingTeamMemberOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            TeamMember teamMember = existingTeamMemberOpt.get();

            // Update fields if provided
            if (name != null) teamMember.setName(name);
            if (role != null) teamMember.setRole(role);
            if (bio != null) teamMember.setBio(bio);
            if (skills != null) teamMember.setSkills(skills);
            if (experienceYears != null) teamMember.setExperienceYears(experienceYears);
            if (email != null) teamMember.setEmail(email);
            if (linkedinUrl != null) teamMember.setLinkedinUrl(linkedinUrl);
            if (twitterUrl != null) teamMember.setTwitterUrl(twitterUrl);
            if (portfolioUrl != null) teamMember.setPortfolioUrl(portfolioUrl);
            if (isActive != null) teamMember.setIsActive(isActive);
            if (sortOrder != null) teamMember.setSortOrder(sortOrder);
            if (department != null) teamMember.setDepartment(department);
            if (location != null) teamMember.setLocation(location);

            // Update hourly rate
            if (hourlyRate != null && !hourlyRate.trim().isEmpty()) {
                try {
                    teamMember.setHourlyRate(new BigDecimal(hourlyRate));
                } catch (NumberFormatException e) {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Invalid hourly rate format");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                }
            }

            // Handle image upload and replacement
            if (image != null && !image.isEmpty()) {
                // Delete old image if exists
                if (teamMember.getImageUrl() != null) {
                    cloudinaryService.deleteImage(teamMember.getImageUrl());
                }

                // Upload new image
                String imageUrl = cloudinaryService.uploadTeamMemberImage(image, teamMember.getId());
                teamMember.setImageUrl(imageUrl);
            }

            TeamMember updatedTeamMember = teamMemberService.saveTeamMember(teamMember);
            return ResponseEntity.ok(updatedTeamMember);

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

    // Delete team member
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeamMember(@PathVariable UUID id) {
        try {
            Optional<TeamMember> teamMemberOpt = teamMemberService.findById(id);
            if (teamMemberOpt.isPresent()) {
                TeamMember teamMember = teamMemberOpt.get();

                // Delete image from Cloudinary before deleting team member
                if (teamMember.getImageUrl() != null) {
                    cloudinaryService.deleteImage(teamMember.getImageUrl());
                }

                // Delete team member from database
                if (teamMemberService.deleteTeamMember(id)) {
                    return ResponseEntity.noContent().build();
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Upload image to existing team member
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadTeamMemberImage(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file) {

        try {
            Optional<TeamMember> teamMemberOpt = teamMemberService.findById(id);
            if (teamMemberOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            TeamMember teamMember = teamMemberOpt.get();

            // Delete old image if exists
            if (teamMember.getImageUrl() != null) {
                cloudinaryService.deleteImage(teamMember.getImageUrl());
            }

            // Upload new image
            String imageUrl = cloudinaryService.uploadTeamMemberImage(file, id);
            teamMember.setImageUrl(imageUrl);

            TeamMember updatedTeamMember = teamMemberService.saveTeamMember(teamMember);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Team member image uploaded successfully");
            response.put("imageUrl", imageUrl);
            response.put("thumbnailUrl", cloudinaryService.getThumbnailUrl(imageUrl));
            response.put("teamMember", updatedTeamMember);

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

    // Get team member image URLs with different sizes
    @GetMapping("/{id}/image")
    public ResponseEntity<Map<String, String>> getTeamMemberImageUrls(@PathVariable UUID id) {
        try {
            Optional<TeamMember> teamMember = teamMemberService.findById(id);
            if (teamMember.isPresent() && teamMember.get().getImageUrl() != null) {
                String originalUrl = teamMember.get().getImageUrl();
                Map<String, String> imageUrls = new HashMap<>();
                imageUrls.put("original", originalUrl);
                imageUrls.put("thumbnail", cloudinaryService.getThumbnailUrl(originalUrl));
                imageUrls.put("medium", cloudinaryService.getMediumImageUrl(originalUrl));
                imageUrls.put("large", cloudinaryService.getLargeImageUrl(originalUrl));
                return ResponseEntity.ok(imageUrls);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get team member count
    @GetMapping("/count")
    public ResponseEntity<Long> getTeamMemberCount() {
        try {
            long count = teamMemberService.getTeamMembersCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Check if team member exists
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> teamMemberExists(@PathVariable UUID id) {
        try {
            boolean exists = teamMemberService.existsById(id);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
