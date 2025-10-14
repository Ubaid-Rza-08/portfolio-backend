package com.example.backend.service;

import com.example.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserDataService {

    @Autowired
    private UserService userService;

    @Autowired
    private ContactInfoService contactInfoService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private TestimonialService testimonialService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private EventService eventService;

    @Autowired
    private PricingPlanService pricingPlanService;

    @Autowired
    private FAQService faqService;

    @Autowired
    private AppointmentService appointmentService;

    /**
     * Get complete user data including all entities
     */
    public Map<String, Object> getCompleteUserData(UUID userId) {
        try {
            Map<String, Object> userData = new HashMap<>();

            // Get user profile with better error handling
            Optional<User> userOpt = getUserById(userId);
            if (userOpt.isEmpty()) {
                System.out.println("DEBUG: User not found with ID: " + userId);
                return null; // User not found
            }
            User user = userOpt.get();
            userData.put("profile", user);

            // Get all related data with null checks
            userData.put("contactInfo", contactInfoService.findByUserId(userId).orElse(null));
            userData.put("services", safeGetList(() -> serviceService.findByUserId(userId)));
            userData.put("portfolios", safeGetList(() -> portfolioService.findByUserId(userId)));
            userData.put("testimonials", safeGetList(() -> testimonialService.findByUserId(userId)));
            userData.put("blogs", safeGetList(() -> blogService.findPublishedByUserId(userId)));
            userData.put("achievements", safeGetList(() -> achievementService.findActiveByUserId(userId)));
            userData.put("teamMembers", safeGetList(() -> teamMemberService.findActiveByUserId(userId)));
            userData.put("events", safeGetList(() -> eventService.findByUserId(userId)));
            userData.put("pricingPlans", safeGetList(() -> pricingPlanService.findActiveByUserId(userId)));
            userData.put("faqs", safeGetList(() -> faqService.findPublishedByUserId(userId)));
            userData.put("appointments", safeGetList(() -> appointmentService.findByUserId(userId)));

            // Add statistics
            userData.put("statistics", generateUserStatistics(userId));

            return userData;
        } catch (Exception e) {
            System.err.println("ERROR in getCompleteUserData: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Get all category data by userId without User entity
     */
    public Map<String, Object> getPublicUserData(UUID userId) {
        try {
            Map<String, Object> userData = new HashMap<>();

            System.out.println("DEBUG: Fetching all category data for user: " + userId);

            // Get all category data with safe calls - no User entity included
            userData.put("contactInfo", contactInfoService.findByUserId(userId).orElse(null));
            userData.put("services", safeGetList(() -> serviceService.findByUserId(userId)));
            userData.put("portfolios", safeGetList(() -> portfolioService.findByUserId(userId)));
            userData.put("testimonials", safeGetList(() -> testimonialService.findByUserId(userId)));
            userData.put("blogs", safeGetList(() -> blogService.findPublishedByUserId(userId)));
            userData.put("achievements", safeGetList(() -> achievementService.findActiveByUserId(userId)));
            userData.put("teamMembers", safeGetList(() -> teamMemberService.findActiveByUserId(userId)));
            userData.put("events", safeGetList(() -> eventService.findByUserId(userId)));
            userData.put("pricingPlans", safeGetList(() -> pricingPlanService.findActiveByUserId(userId)));
            userData.put("faqs", safeGetList(() -> faqService.findPublishedByUserId(userId)));
            userData.put("appointments", safeGetList(() -> appointmentService.findByUserId(userId)));

            // Add content summary
            userData.put("contentSummary", getUserContentSummary(userId));

            System.out.println("DEBUG: Successfully prepared all category data for user: " + userId);
            return userData;
        } catch (Exception e) {
            System.err.println("ERROR in getPublicUserData: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Get user dashboard summary with key metrics
     */
//    public Map<String, Object> getUserDashboardData(UUID userId) {
//        try {
//            Map<String, Object> dashboardData = new HashMap<>();
//
//            // Verify user exists
//            Optional<User> userOpt = getUserById(userId);
//            if (userOpt.isEmpty()) {
//                return null; // User not found
//            }
//            User user = userOpt.get();
//
//            // Basic user info
//            Map<String, Object> userInfo = new HashMap<>();
//            userInfo.put("id", user.getId());
//            userInfo.put("name", user.getName());
//            userInfo.put("title", user.getTitle());
//            userInfo.put("email", user.getEmail());
//            userInfo.put("profileImageUrl", user.getProfileImageUrl());
//            dashboardData.put("user", userInfo);
//
//            // Get entity counts
//            dashboardData.put("counts", generateUserCounts(userId));
//
//            // Recent activities
//            dashboardData.put("recentActivities", generateRecentActivities(userId));
//
//            // Quick statistics
//            dashboardData.put("quickStats", generateQuickStats(userId));
//
//            return dashboardData;
//        } catch (Exception e) {
//            System.err.println("ERROR in getUserDashboardData: " + e.getMessage());
//            e.printStackTrace();
//            throw e;
//        }
//    }

    /**
     * Get user data by specific category
     */
    public Map<String, Object> getUserDataByCategory(UUID userId, String category) {
        try {
            Map<String, Object> categoryData = new HashMap<>();

            switch (category.toLowerCase()) {
                case "services":
                    categoryData.put("services", safeGetList(() -> serviceService.findByUserId(userId)));
                    break;
                case "portfolios":
                    categoryData.put("portfolios", safeGetList(() -> portfolioService.findByUserId(userId)));
                    break;
                case "testimonials":
                    categoryData.put("testimonials", safeGetList(() -> testimonialService.findByUserId(userId)));
                    break;
                case "blogs":
                    categoryData.put("blogs", safeGetList(() -> blogService.findPublishedByUserId(userId)));
                    break;
                case "achievements":
                    categoryData.put("achievements", safeGetList(() -> achievementService.findActiveByUserId(userId)));
                    break;
                case "team":
                    categoryData.put("teamMembers", safeGetList(() -> teamMemberService.findActiveByUserId(userId)));
                    break;
                case "events":
                    categoryData.put("events", safeGetList(() -> eventService.findByUserId(userId)));
                    break;
                case "pricing":
                    categoryData.put("pricingPlans", safeGetList(() -> pricingPlanService.findActiveByUserId(userId)));
                    break;
                case "faqs":
                    categoryData.put("faqs", safeGetList(() -> faqService.findPublishedByUserId(userId)));
                    break;
                case "appointments":
                    categoryData.put("appointments", safeGetList(() -> appointmentService.findByUserId(userId)));
                    break;
                case "contactinfo":
                    categoryData.put("contactInfo", contactInfoService.findByUserId(userId).orElse(null));
                    break;
                default:
                    return null; // Invalid category
            }

            return categoryData;
        } catch (Exception e) {
            System.err.println("ERROR in getUserDataByCategory: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Export complete user data with metadata
     */
    public Map<String, Object> exportUserData(UUID userId) {
        try {
            Map<String, Object> exportData = new HashMap<>();

            // Add metadata
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("exportDate", new Date());
            metadata.put("userId", userId);
            metadata.put("version", "1.0");
            exportData.put("metadata", metadata);

            // Get complete data
            Map<String, Object> completeData = getCompleteUserData(userId);
            if (completeData == null) {
                return null; // User not found
            }

            exportData.putAll(completeData);
            return exportData;
        } catch (Exception e) {
            System.err.println("ERROR in exportUserData: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Check if user exists and return basic information
     */
//    public Map<String, Object> checkUserExists(UUID userId) {
//        try {
//            Optional<User> userOpt = getUserById(userId);
//            Map<String, Object> response = new HashMap<>();
//
//            response.put("exists", userOpt.isPresent());
//
//            if (userOpt.isPresent()) {
//                User user = userOpt.get();
//                Map<String, Object> basicInfo = new HashMap<>();
//                basicInfo.put("id", user.getId());
//                basicInfo.put("name", user.getName());
//                basicInfo.put("title", user.getTitle());
//                basicInfo.put("email", user.getEmail());
//                response.put("user", basicInfo);
//            }
//
//            return response;
//        } catch (Exception e) {
//            System.err.println("ERROR in checkUserExists: " + e.getMessage());
//            e.printStackTrace();
//            throw e;
//        }
//    }

    // Private helper methods

    /**
     * Safe method to get user by ID that handles the List<User> return from UserService
     */
    private Optional<User> getUserById(UUID userId) {
        try {
            List<User> userList = userService.findById(userId);
            if (userList != null && !userList.isEmpty()) {
                return Optional.of(userList.get(0));
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("ERROR in getUserById: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Safe method to get lists that handles potential null/exceptions
     */
    private <T> List<T> safeGetList(java.util.function.Supplier<List<T>> supplier) {
        try {
            List<T> result = supplier.get();
            return result != null ? result : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("ERROR in safeGetList: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private Map<String, Long> generateUserStatistics(UUID userId) {
        Map<String, Long> stats = new HashMap<>();

        stats.put("servicesCount", (long) safeGetList(() -> serviceService.findByUserId(userId)).size());
        stats.put("portfoliosCount", (long) safeGetList(() -> portfolioService.findByUserId(userId)).size());
        stats.put("testimonialsCount", (long) safeGetList(() -> testimonialService.findByUserId(userId)).size());
        stats.put("blogsCount", (long) safeGetList(() -> blogService.findPublishedByUserId(userId)).size());
        stats.put("achievementsCount", (long) safeGetList(() -> achievementService.findActiveByUserId(userId)).size());
        stats.put("teamMembersCount", (long) safeGetList(() -> teamMemberService.findActiveByUserId(userId)).size());
        stats.put("eventsCount", (long) safeGetList(() -> eventService.findByUserId(userId)).size());
        stats.put("pricingPlansCount", (long) safeGetList(() -> pricingPlanService.findActiveByUserId(userId)).size());
        stats.put("faqsCount", (long) safeGetList(() -> faqService.findPublishedByUserId(userId)).size());
        stats.put("appointmentsCount", (long) safeGetList(() -> appointmentService.findByUserId(userId)).size());

        return stats;
    }

    private Map<String, Long> generateUserCounts(UUID userId) {
        Map<String, Long> counts = new HashMap<>();

        try {
            counts.put("services", serviceService.getServicesCount());
            counts.put("portfolios", portfolioService.getPortfoliosCount());
            counts.put("testimonials", testimonialService.getTestimonialsCount());
            counts.put("teamMembers", teamMemberService.getTeamMembersCount());
            counts.put("upcomingAppointments", (long) safeGetList(() -> appointmentService.findUpcomingByUserId(userId)).size());
            counts.put("upcomingEvents", (long) safeGetList(() -> eventService.findUpcomingByUserId(userId)).size());
        } catch (Exception e) {
            System.err.println("ERROR generating user counts: " + e.getMessage());
            // Return empty counts instead of failing
            counts.put("services", 0L);
            counts.put("portfolios", 0L);
            counts.put("testimonials", 0L);
            counts.put("teamMembers", 0L);
            counts.put("upcomingAppointments", 0L);
            counts.put("upcomingEvents", 0L);
        }

        return counts;
    }

    private Map<String, Object> generateRecentActivities(UUID userId) {
        Map<String, Object> recentActivities = new HashMap<>();

        List<Blog> recentBlogs = safeGetList(() -> blogService.findPublishedByUserId(userId));
        recentActivities.put("recentBlogs", recentBlogs.stream().limit(5).toList());

        List<Testimonial> recentTestimonials = safeGetList(() -> testimonialService.findByUserId(userId));
        recentActivities.put("recentTestimonials", recentTestimonials.stream().limit(5).toList());

        List<Appointment> upcomingAppointments = safeGetList(() -> appointmentService.findUpcomingByUserId(userId));
        recentActivities.put("upcomingAppointments", upcomingAppointments.stream().limit(5).toList());

        return recentActivities;
    }

    private Map<String, Object> generateQuickStats(UUID userId) {
        Map<String, Object> quickStats = new HashMap<>();

        quickStats.put("totalPortfolioViews", safeGetList(() -> portfolioService.findByUserId(userId)).size());
        quickStats.put("averageTestimonialRating", calculateAverageRating(safeGetList(() -> testimonialService.findApprovedByUserId(userId))));
        quickStats.put("totalBlogViews", calculateTotalBlogViews(safeGetList(() -> blogService.findPublishedByUserId(userId))));

        return quickStats;
    }

    private double calculateAverageRating(List<Testimonial> testimonials) {
        if (testimonials == null || testimonials.isEmpty()) {
            return 0.0;
        }
        return testimonials.stream()
                .filter(t -> t.getRating() != null)
                .mapToInt(Testimonial::getRating)
                .average()
                .orElse(0.0);
    }

    private long calculateTotalBlogViews(List<Blog> blogs) {
        if (blogs == null || blogs.isEmpty()) {
            return 0L;
        }
        return blogs.stream()
                .filter(b -> b.getViewCount() != null)
                .mapToLong(Blog::getViewCount)
                .sum();
    }

    /**
     * Check if user has any content
     */
    public boolean hasUserContent(UUID userId) {
        return !safeGetList(() -> serviceService.findByUserId(userId)).isEmpty() ||
                !safeGetList(() -> portfolioService.findByUserId(userId)).isEmpty() ||
                !safeGetList(() -> testimonialService.findByUserId(userId)).isEmpty() ||
                !safeGetList(() -> blogService.findPublishedByUserId(userId)).isEmpty() ||
                !safeGetList(() -> achievementService.findActiveByUserId(userId)).isEmpty() ||
                !safeGetList(() -> teamMemberService.findActiveByUserId(userId)).isEmpty() ||
                !safeGetList(() -> eventService.findByUserId(userId)).isEmpty() ||
                !safeGetList(() -> pricingPlanService.findActiveByUserId(userId)).isEmpty() ||
                !safeGetList(() -> faqService.findPublishedByUserId(userId)).isEmpty() ||
                !safeGetList(() -> appointmentService.findByUserId(userId)).isEmpty();
    }

    /**
     * Get content summary for user
     */
    public Map<String, Integer> getUserContentSummary(UUID userId) {
        Map<String, Integer> summary = new HashMap<>();

        summary.put("services", safeGetList(() -> serviceService.findByUserId(userId)).size());
        summary.put("portfolios", safeGetList(() -> portfolioService.findByUserId(userId)).size());
        summary.put("testimonials", safeGetList(() -> testimonialService.findByUserId(userId)).size());
        summary.put("blogs", safeGetList(() -> blogService.findPublishedByUserId(userId)).size());
        summary.put("achievements", safeGetList(() -> achievementService.findActiveByUserId(userId)).size());
        summary.put("teamMembers", safeGetList(() -> teamMemberService.findActiveByUserId(userId)).size());
        summary.put("events", safeGetList(() -> eventService.findByUserId(userId)).size());
        summary.put("pricingPlans", safeGetList(() -> pricingPlanService.findActiveByUserId(userId)).size());
        summary.put("faqs", safeGetList(() -> faqService.findPublishedByUserId(userId)).size());
        summary.put("appointments", safeGetList(() -> appointmentService.findByUserId(userId)).size());
        summary.put("contactInfo", contactInfoService.findByUserId(userId).isPresent() ? 1 : 0);

        return summary;
    }
}