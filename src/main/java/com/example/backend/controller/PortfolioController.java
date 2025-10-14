// Comprehensive Portfolio Controller Example
package com.example.backend.controller;

import com.example.backend.entity.Portfolio;
import com.example.backend.service.PortfolioService;
import com.example.backend.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/portfolios")
@CrossOrigin(origins = "*")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private CloudinaryService cloudinaryService;

    // Get all portfolios for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Portfolio>> getPortfoliosByUserId(@PathVariable UUID userId) {
        List<Portfolio> portfolios = portfolioService.findByUserId(userId);
        return ResponseEntity.ok(portfolios);
    }

    // Get featured portfolios for a user
    @GetMapping("/user/{userId}/featured")
    public ResponseEntity<List<Portfolio>> getFeaturedPortfolios(@PathVariable UUID userId) {
        List<Portfolio> portfolios = portfolioService.findFeaturedByUserId(userId);
        return ResponseEntity.ok(portfolios);
    }

    // Get portfolio by ID
    @GetMapping("/{id}")
    public ResponseEntity<Portfolio> getPortfolioById(@PathVariable UUID id) {
        Optional<Portfolio> portfolio = portfolioService.findById(id);
        return portfolio.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Search portfolios by title
    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<Portfolio>> searchPortfolios(
            @PathVariable UUID userId,
            @RequestParam String title) {
        List<Portfolio> portfolios = portfolioService.searchByTitle(userId, title);
        return ResponseEntity.ok(portfolios);
    }

    // Get portfolios by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Portfolio>> getPortfoliosByCategory(@PathVariable String category) {
        List<Portfolio> portfolios = portfolioService.findByCategory(category);
        return ResponseEntity.ok(portfolios);
    }

    // Create new portfolio with images
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPortfolio(
            @RequestParam UUID userId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String projectDate,
            @RequestParam(required = false) String projectUrl,
            @RequestParam(required = false) String githubUrl,
            @RequestParam(required = false) String technologies,
            @RequestParam(required = false) Boolean isFeatured,
            @RequestParam(required = false) Integer sortOrder,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer completionPercentage,
            @RequestParam(required = false) String testimonial,
            @RequestParam(required = false) String testimonialAuthor,
            @RequestParam(required = false) String videoUrl,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) List<MultipartFile> images) {

        try {
            Portfolio portfolio = new Portfolio();
            portfolio.setUserId(userId);
            portfolio.setTitle(title);
            portfolio.setDescription(description);
            portfolio.setCategory(category);
            portfolio.setClientName(clientName);
            portfolio.setProjectUrl(projectUrl);
            portfolio.setGithubUrl(githubUrl);
            portfolio.setTechnologies(technologies);
            portfolio.setIsFeatured(isFeatured != null ? isFeatured : false);
            portfolio.setSortOrder(sortOrder != null ? sortOrder : 0);
            portfolio.setStatus(status != null ? status : "completed");
            portfolio.setCompletionPercentage(completionPercentage != null ? completionPercentage : 100);
            portfolio.setTestimonial(testimonial);
            portfolio.setTestimonialAuthor(testimonialAuthor);
            portfolio.setVideoUrl(videoUrl);
            portfolio.setTags(tags);

            // Parse project date
            if (projectDate != null && !projectDate.trim().isEmpty()) {
                portfolio.setProjectDate(LocalDate.parse(projectDate));
            }

            // Save portfolio first to get ID
            Portfolio savedPortfolio = portfolioService.savePortfolio(portfolio);

            // Handle image uploads
            if (images != null && !images.isEmpty()) {
                List<String> imageUrls = new ArrayList<>();
                for (int i = 0; i < images.size() && i < 10; i++) { // Limit to 10 images
                    MultipartFile image = images.get(i);
                    if (!image.isEmpty()) {
                        String imageUrl = cloudinaryService.uploadPortfolioImage(image, savedPortfolio.getId(), i + 1);
                        imageUrls.add(imageUrl);
                    }
                }

                // Convert to JSON array string
                if (!imageUrls.isEmpty()) {
                    savedPortfolio.setImageUrls(convertListToJson(imageUrls));
                    savedPortfolio = portfolioService.savePortfolio(savedPortfolio);
                }
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedPortfolio);

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

    // Update portfolio
    @PutMapping("/{id}")
    public ResponseEntity<Portfolio> updatePortfolio(@PathVariable UUID id, @RequestBody Portfolio portfolioDetails) {
        try {
            Portfolio updatedPortfolio = portfolioService.updatePortfolio(id, portfolioDetails);
            return ResponseEntity.ok(updatedPortfolio);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update portfolio with form data and images
    @PutMapping(value = "/{id}/form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePortfolioWithForm(
            @PathVariable UUID id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String projectDate,
            @RequestParam(required = false) String projectUrl,
            @RequestParam(required = false) String githubUrl,
            @RequestParam(required = false) String technologies,
            @RequestParam(required = false) Boolean isFeatured,
            @RequestParam(required = false) Integer sortOrder,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer completionPercentage,
            @RequestParam(required = false) String testimonial,
            @RequestParam(required = false) String testimonialAuthor,
            @RequestParam(required = false) String videoUrl,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) List<MultipartFile> newImages) {

        try {
            Optional<Portfolio> existingPortfolioOpt = portfolioService.findById(id);
            if (existingPortfolioOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Portfolio portfolio = existingPortfolioOpt.get();

            // Update fields if provided
            if (title != null) portfolio.setTitle(title);
            if (description != null) portfolio.setDescription(description);
            if (category != null) portfolio.setCategory(category);
            if (clientName != null) portfolio.setClientName(clientName);
            if (projectUrl != null) portfolio.setProjectUrl(projectUrl);
            if (githubUrl != null) portfolio.setGithubUrl(githubUrl);
            if (technologies != null) portfolio.setTechnologies(technologies);
            if (isFeatured != null) portfolio.setIsFeatured(isFeatured);
            if (sortOrder != null) portfolio.setSortOrder(sortOrder);
            if (status != null) portfolio.setStatus(status);
            if (completionPercentage != null) portfolio.setCompletionPercentage(completionPercentage);
            if (testimonial != null) portfolio.setTestimonial(testimonial);
            if (testimonialAuthor != null) portfolio.setTestimonialAuthor(testimonialAuthor);
            if (videoUrl != null) portfolio.setVideoUrl(videoUrl);
            if (tags != null) portfolio.setTags(tags);

            // Parse project date
            if (projectDate != null && !projectDate.trim().isEmpty()) {
                portfolio.setProjectDate(LocalDate.parse(projectDate));
            }

            // Handle new image uploads
            if (newImages != null && !newImages.isEmpty()) {
                // Delete old images if they exist
                if (portfolio.getImageUrls() != null) {
                    List<String> oldUrls = parseJsonToList(portfolio.getImageUrls());
                    for (String url : oldUrls) {
                        cloudinaryService.deleteImage(url);
                    }
                }

                // Upload new images
                List<String> imageUrls = new ArrayList<>();
                for (int i = 0; i < newImages.size() && i < 10; i++) {
                    MultipartFile image = newImages.get(i);
                    if (!image.isEmpty()) {
                        String imageUrl = cloudinaryService.uploadPortfolioImage(image, portfolio.getId(), i + 1);
                        imageUrls.add(imageUrl);
                    }
                }

                portfolio.setImageUrls(convertListToJson(imageUrls));
            }

            Portfolio updatedPortfolio = portfolioService.savePortfolio(portfolio);
            return ResponseEntity.ok(updatedPortfolio);

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

    // Delete portfolio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable UUID id) {
        try {
            Optional<Portfolio> portfolioOpt = portfolioService.findById(id);
            if (portfolioOpt.isPresent()) {
                Portfolio portfolio = portfolioOpt.get();

                // Delete all images from Cloudinary
                if (portfolio.getImageUrls() != null) {
                    List<String> imageUrls = parseJsonToList(portfolio.getImageUrls());
                    for (String url : imageUrls) {
                        cloudinaryService.deleteImage(url);
                    }
                }

                if (portfolioService.deletePortfolio(id)) {
                    return ResponseEntity.noContent().build();
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Upload single image to existing portfolio
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImageToPortfolio(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file) {

        try {
            Optional<Portfolio> portfolioOpt = portfolioService.findById(id);
            if (portfolioOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Portfolio portfolio = portfolioOpt.get();

            // Get current images
            List<String> currentUrls = portfolio.getImageUrls() != null ?
                    parseJsonToList(portfolio.getImageUrls()) : new ArrayList<>();

            // Upload new image
            int imageIndex = currentUrls.size() + 1;
            String imageUrl = cloudinaryService.uploadPortfolioImage(file, id, imageIndex);
            currentUrls.add(imageUrl);

            // Update portfolio
            portfolio.setImageUrls(convertListToJson(currentUrls));
            Portfolio updatedPortfolio = portfolioService.savePortfolio(portfolio);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Image uploaded successfully");
            response.put("imageUrl", imageUrl);
            response.put("portfolio", updatedPortfolio);

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

    // Get portfolio count
    @GetMapping("/count")
    public ResponseEntity<Long> getPortfolioCount() {
        long count = portfolioService.getPortfoliosCount();
        return ResponseEntity.ok(count);
    }

    // Helper methods
    private String convertListToJson(List<String> list) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            json.append("\"").append(list.get(i)).append("\"");
            if (i < list.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    private List<String> parseJsonToList(String json) {
        List<String> list = new ArrayList<>();
        if (json != null && json.startsWith("[") && json.endsWith("]")) {
            String content = json.substring(1, json.length() - 1);
            if (!content.trim().isEmpty()) {
                String[] items = content.split(",");
                for (String item : items) {
                    String cleanItem = item.trim().replaceAll("^\"|\"$", "");
                    list.add(cleanItem);
                }
            }
        }
        return list;
    }
}