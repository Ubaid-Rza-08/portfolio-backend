package com.example.backend.controller;


import com.example.backend.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-data")
@CrossOrigin(origins = "*")
public class UserDataController {

    @Autowired
    private UserDataService userDataService;

    /**
     * Get complete user data including all entities
     * Returns everything for a user (private admin view)
     */
    @GetMapping("/{userId}/complete")
    public ResponseEntity<?> getCompleteUserData(@PathVariable UUID userId) {
        try {
            Map<String, Object> userData = userDataService.getCompleteUserData(userId);

            if (userData == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(userData);

        } catch (Exception e) {
            return handleException("Failed to fetch complete user data", e);
        }
    }

    /**
     * Get only public/published content for portfolio display
     * Returns filtered content suitable for public portfolio websites
     */
    @GetMapping("/{userId}/public")
    public ResponseEntity<?> getPublicUserData(@PathVariable UUID userId) {
        try {
            Map<String, Object> userData = userDataService.getPublicUserData(userId);

            if (userData == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(userData);

        } catch (Exception e) {
            return handleException("Failed to fetch public user data", e);
        }
    }

    /**
     * Get user dashboard summary with key metrics
     * Returns overview data for admin dashboard
     */
//    @GetMapping("/{userId}/dashboard")
//    public ResponseEntity<?> getUserDashboard(@PathVariable UUID userId) {
//        try {
//            Map<String, Object> dashboardData = userDataService.getUserDashboardData(userId);
//
//            if (dashboardData == null) {
//                return ResponseEntity.notFound().build();
//            }
//
//            return ResponseEntity.ok(dashboardData);
//
//        } catch (Exception e) {
//            return handleException("Failed to fetch dashboard data", e);
//        }
//    }

    /**
     * Get user data by specific category
     * Returns data for a single content type
     */
    @GetMapping("/{userId}/category/{category}")
    public ResponseEntity<?> getUserDataByCategory(
            @PathVariable UUID userId,
            @PathVariable String category) {
        try {
            Map<String, Object> categoryData = userDataService.getUserDataByCategory(userId, category);

            if (categoryData == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid category: " + category + ". Valid categories: services, portfolios, testimonials, blogs, achievements, team, events, pricing, faqs, appointments");
                return ResponseEntity.badRequest().body(error);
            }

            return ResponseEntity.ok(categoryData);

        } catch (Exception e) {
            return handleException("Failed to fetch category data", e);
        }
    }

    /**
     * Export complete user data with metadata
     * Returns all user data formatted for backup/migration
     */
    @GetMapping("/{userId}/export")
    public ResponseEntity<?> exportUserData(@PathVariable UUID userId) {
        try {
            Map<String, Object> exportData = userDataService.exportUserData(userId);

            if (exportData == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(exportData);

        } catch (Exception e) {
            return handleException("Failed to export user data", e);
        }
    }

    /**
     * Check if user exists and get basic info
     * Returns user existence status and basic information
     */
//    @GetMapping("/{userId}/exists")
//    public ResponseEntity<?> checkUserExists(@PathVariable UUID userId) {
//        try {
//            Map<String, Object> response = userDataService.checkUserExists(userId);
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            return handleException("Failed to check user existence", e);
//        }
//    }

    /**
     * Check if user has any content
     * Returns boolean indicating if user has created any content
     */
    @GetMapping("/{userId}/has-content")
    public ResponseEntity<?> checkUserHasContent(@PathVariable UUID userId) {
        try {
            boolean hasContent = userDataService.hasUserContent(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("hasContent", hasContent);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return handleException("Failed to check user content", e);
        }
    }

    /**
     * Get content summary for user
     * Returns count of items in each category
     */
    @GetMapping("/{userId}/summary")
    public ResponseEntity<?> getUserContentSummary(@PathVariable UUID userId) {
        try {
            Map<String, Integer> summary = userDataService.getUserContentSummary(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("contentSummary", summary);
            response.put("totalItems", summary.values().stream().mapToInt(Integer::intValue).sum());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return handleException("Failed to fetch content summary", e);
        }
    }

    /**
     * Get available categories list
     * Returns list of valid categories for the category endpoint
     */
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getAvailableCategories() {
        Map<String, Object> response = new HashMap<>();

        String[] categories = {
                "services", "portfolios", "testimonials", "blogs",
                "achievements", "team", "events", "pricing", "faqs", "appointments"
        };

        response.put("categories", categories);
        response.put("description", "Available categories for /api/user-data/{userId}/category/{category}");

        return ResponseEntity.ok(response);
    }

    /**
     * Get API documentation
     * Returns information about available endpoints
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> info = new HashMap<>();

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("GET /{userId}/complete", "Get all user data (admin view)");
        endpoints.put("GET /{userId}/public", "Get public portfolio data");
        endpoints.put("GET /{userId}/dashboard", "Get dashboard summary");
        endpoints.put("GET /{userId}/category/{category}", "Get specific category data");
        endpoints.put("GET /{userId}/export", "Export user data with metadata");
        endpoints.put("GET /{userId}/exists", "Check if user exists");
        endpoints.put("GET /{userId}/has-content", "Check if user has content");
        endpoints.put("GET /{userId}/summary", "Get content counts summary");
        endpoints.put("GET /categories", "Get available categories");

        info.put("endpoints", endpoints);
        info.put("baseUrl", "/api/user-data");
        info.put("description", "Comprehensive user data aggregation API");

        return ResponseEntity.ok(info);
    }

    // Private helper method for consistent error handling
    private ResponseEntity<Map<String, String>> handleException(String message, Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message + ": " + e.getMessage());
        error.put("type", e.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
