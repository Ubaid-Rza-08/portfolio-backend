package com.example.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "testimonials")
public class Testimonial {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "client_title")
    private String clientTitle;

    @Column(name = "client_company")
    private String clientCompany;

    @Column(name = "testimonial_text", columnDefinition = "TEXT", nullable = false)
    private String testimonialText;

    @Column(name = "client_image_url")
    private String clientImageUrl;

    @Column(name = "rating")
    private Integer rating; // 1-5 stars

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "testimonial_date")
    private LocalDate testimonialDate;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "is_approved")
    private Boolean isApproved = true;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "client_website")
    private String clientWebsite;

    @Column(name = "client_linkedin")
    private String clientLinkedin;

    @Column(name = "video_testimonial_url")
    private String videoTestimonialUrl;

    @Column(name = "audio_testimonial_url")
    private String audioTestimonialUrl;

    // Constructors
    public Testimonial() {}

    public Testimonial(UUID userId, String clientName, String testimonialText) {
        this.userId = userId;
        this.clientName = clientName;
        this.testimonialText = testimonialText;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getClientTitle() { return clientTitle; }
    public void setClientTitle(String clientTitle) { this.clientTitle = clientTitle; }

    public String getClientCompany() { return clientCompany; }
    public void setClientCompany(String clientCompany) { this.clientCompany = clientCompany; }

    public String getTestimonialText() { return testimonialText; }
    public void setTestimonialText(String testimonialText) { this.testimonialText = testimonialText; }

    public String getClientImageUrl() { return clientImageUrl; }
    public void setClientImageUrl(String clientImageUrl) { this.clientImageUrl = clientImageUrl; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public LocalDate getTestimonialDate() { return testimonialDate; }
    public void setTestimonialDate(LocalDate testimonialDate) { this.testimonialDate = testimonialDate; }

    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }

    public Boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public String getClientWebsite() { return clientWebsite; }
    public void setClientWebsite(String clientWebsite) { this.clientWebsite = clientWebsite; }

    public String getClientLinkedin() { return clientLinkedin; }
    public void setClientLinkedin(String clientLinkedin) { this.clientLinkedin = clientLinkedin; }

    public String getVideoTestimonialUrl() { return videoTestimonialUrl; }
    public void setVideoTestimonialUrl(String videoTestimonialUrl) { this.videoTestimonialUrl = videoTestimonialUrl; }

    public String getAudioTestimonialUrl() { return audioTestimonialUrl; }
    public void setAudioTestimonialUrl(String audioTestimonialUrl) { this.audioTestimonialUrl = audioTestimonialUrl; }
}