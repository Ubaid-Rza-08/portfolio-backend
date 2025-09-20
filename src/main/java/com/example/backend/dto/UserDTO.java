package com.example.backend.dto;

import java.time.LocalTime;
import java.util.UUID;

public class UserDTO {
    private UUID id;
    private String name;
    private String title;
    private String description;
    private String location;
    private Integer experienceYear;
    private String email;
    private String phone;
    private String faceBookUrl;
    private String linkedInUrl;
    private String twitterUrl;
    private String instaUrl;
    private String dribbleUrl;
    private String otherUrl;
    private String day;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String serviceTitle;
    private String serviceDescription;
    private String description1;
    private String description2;
    private String description3;
    private String description4;
    private boolean hasProfileImage;
    private boolean hasImage1;
    private boolean hasImage2;
    private boolean hasImage3;
    private boolean hasImage4;

    // Default constructor
    public UserDTO() {}

    // Constructor with all fields except images
    public UserDTO(UUID id, String name, String title, String description, String location,
                   Integer experienceYear, String email, String phone, String faceBookUrl,
                   String linkedInUrl, String twitterUrl, String instaUrl, String dribbleUrl,
                   String otherUrl, String day, LocalTime openTime, LocalTime closeTime,
                   String serviceTitle, String serviceDescription, String description1,
                   String description2, String description3, String description4,
                   boolean hasProfileImage, boolean hasImage1, boolean hasImage2,
                   boolean hasImage3, boolean hasImage4) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.description = description;
        this.location = location;
        this.experienceYear = experienceYear;
        this.email = email;
        this.phone = phone;
        this.faceBookUrl = faceBookUrl;
        this.linkedInUrl = linkedInUrl;
        this.twitterUrl = twitterUrl;
        this.instaUrl = instaUrl;
        this.dribbleUrl = dribbleUrl;
        this.otherUrl = otherUrl;
        this.day = day;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.serviceTitle = serviceTitle;
        this.serviceDescription = serviceDescription;
        this.description1 = description1;
        this.description2 = description2;
        this.description3 = description3;
        this.description4 = description4;
        this.hasProfileImage = hasProfileImage;
        this.hasImage1 = hasImage1;
        this.hasImage2 = hasImage2;
        this.hasImage3 = hasImage3;
        this.hasImage4 = hasImage4;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getExperienceYear() { return experienceYear; }
    public void setExperienceYear(Integer experienceYear) { this.experienceYear = experienceYear; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getFaceBookUrl() { return faceBookUrl; }
    public void setFaceBookUrl(String faceBookUrl) { this.faceBookUrl = faceBookUrl; }

    public String getLinkedInUrl() { return linkedInUrl; }
    public void setLinkedInUrl(String linkedInUrl) { this.linkedInUrl = linkedInUrl; }

    public String getTwitterUrl() { return twitterUrl; }
    public void setTwitterUrl(String twitterUrl) { this.twitterUrl = twitterUrl; }

    public String getInstaUrl() { return instaUrl; }
    public void setInstaUrl(String instaUrl) { this.instaUrl = instaUrl; }

    public String getDribbleUrl() { return dribbleUrl; }
    public void setDribbleUrl(String dribbleUrl) { this.dribbleUrl = dribbleUrl; }

    public String getOtherUrl() { return otherUrl; }
    public void setOtherUrl(String otherUrl) { this.otherUrl = otherUrl; }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public LocalTime getOpenTime() { return openTime; }
    public void setOpenTime(LocalTime openTime) { this.openTime = openTime; }

    public LocalTime getCloseTime() { return closeTime; }
    public void setCloseTime(LocalTime closeTime) { this.closeTime = closeTime; }

    public String getServiceTitle() { return serviceTitle; }
    public void setServiceTitle(String serviceTitle) { this.serviceTitle = serviceTitle; }

    public String getServiceDescription() { return serviceDescription; }
    public void setServiceDescription(String serviceDescription) { this.serviceDescription = serviceDescription; }

    public String getDescription1() { return description1; }
    public void setDescription1(String description1) { this.description1 = description1; }

    public String getDescription2() { return description2; }
    public void setDescription2(String description2) { this.description2 = description2; }

    public String getDescription3() { return description3; }
    public void setDescription3(String description3) { this.description3 = description3; }

    public String getDescription4() { return description4; }
    public void setDescription4(String description4) { this.description4 = description4; }

    public boolean isHasProfileImage() { return hasProfileImage; }
    public void setHasProfileImage(boolean hasProfileImage) { this.hasProfileImage = hasProfileImage; }

    public boolean isHasImage1() { return hasImage1; }
    public void setHasImage1(boolean hasImage1) { this.hasImage1 = hasImage1; }

    public boolean isHasImage2() { return hasImage2; }
    public void setHasImage2(boolean hasImage2) { this.hasImage2 = hasImage2; }

    public boolean isHasImage3() { return hasImage3; }
    public void setHasImage3(boolean hasImage3) { this.hasImage3 = hasImage3; }

    public boolean isHasImage4() { return hasImage4; }
    public void setHasImage4(boolean hasImage4) { this.hasImage4 = hasImage4; }
}