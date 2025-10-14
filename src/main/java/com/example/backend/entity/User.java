package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
//
//    @Column(name = "profile_image_url")
//    private String profileImageUrl;
//
//    @Column(name = "location")
//    private String location;
//
//    @Column(name = "experience_year")
//    private Integer experienceYear;
//
//    @Column(name = "email")
//    private String email;
//
//    @Column(name = "phone")
//    private String phone;
//
//    @Column(name = "facebook_url")
//    private String faceBookUrl;
//
//    @Column(name = "linkedin_url")
//    private String linkedInUrl;
//
//    @Column(name = "twitter_url")
//    private String twitterUrl;
//
//    @Column(name = "insta_url")
//    private String instaUrl;
//
//    @Column(name = "dribble_url")
//    private String dribbleUrl;
//
//    @Column(name = "other_url")
//    private String otherUrl;
//
//    @Column(name = "day")
//    private String day;
//
//    @Column(name = "open_time")
//    private LocalTime openTime;
//
//    @Column(name = "close_time")
//    private LocalTime closeTime;
//
//    @Column(name = "service_title")
//    private String serviceTitle;
//
//    @Column(name = "service_description", columnDefinition = "TEXT")
//    private String serviceDescription;
//
//    @Column(name = "image1_url")
//    private String image1Url;
//
//    @Column(name = "description1", columnDefinition = "TEXT")
//    private String description1;
//
//    @Column(name = "image2_url")
//    private String image2Url;
//
//    @Column(name = "description2", columnDefinition = "TEXT")
//    private String description2;
//
//    @Column(name = "image3_url")
//    private String image3Url;
//
//    @Column(name = "description3", columnDefinition = "TEXT")
//    private String description3;
//
//    @Column(name = "image4_url")
//    private String image4Url;
//
//    @Column(name = "description4", columnDefinition = "TEXT")
//    private String description4;

//    // Default constructor
//    public User() {}
//
//    // Constructor with parameters
//    public User(String name, String title, String description) {
//        this.name = name;
//        this.title = title;
//        this.description = description;
//    }
//
//    // Getters and Setters
//    public UUID getId() {
//        return id;
//    }
//
//    public void setId(UUID id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getProfileImageUrl() {
//        return profileImageUrl;
//    }
//
//    public void setProfileImageUrl(String profileImageUrl) {
//        this.profileImageUrl = profileImageUrl;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public Integer getExperienceYear() {
//        return experienceYear;
//    }
//
//    public void setExperienceYear(Integer experienceYear) {
//        this.experienceYear = experienceYear;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getFaceBookUrl() {
//        return faceBookUrl;
//    }
//
//    public void setFaceBookUrl(String faceBookUrl) {
//        this.faceBookUrl = faceBookUrl;
//    }
//
//    public String getLinkedInUrl() {
//        return linkedInUrl;
//    }
//
//    public void setLinkedInUrl(String linkedInUrl) {
//        this.linkedInUrl = linkedInUrl;
//    }
//
//    public String getTwitterUrl() {
//        return twitterUrl;
//    }
//
//    public void setTwitterUrl(String twitterUrl) {
//        this.twitterUrl = twitterUrl;
//    }
//
//    public String getInstaUrl() {
//        return instaUrl;
//    }
//
//    public void setInstaUrl(String instaUrl) {
//        this.instaUrl = instaUrl;
//    }
//
//    public String getDribbleUrl() {
//        return dribbleUrl;
//    }
//
//    public void setDribbleUrl(String dribbleUrl) {
//        this.dribbleUrl = dribbleUrl;
//    }
//
//    public String getOtherUrl() {
//        return otherUrl;
//    }
//
//    public void setOtherUrl(String otherUrl) {
//        this.otherUrl = otherUrl;
//    }
//
//    public String getDay() {
//        return day;
//    }
//
//    public void setDay(String day) {
//        this.day = day;
//    }
//
//    public LocalTime getOpenTime() {
//        return openTime;
//    }
//
//    public void setOpenTime(LocalTime openTime) {
//        this.openTime = openTime;
//    }
//
//    public LocalTime getCloseTime() {
//        return closeTime;
//    }
//
//    public void setCloseTime(LocalTime closeTime) {
//        this.closeTime = closeTime;
//    }
//
//    public String getServiceTitle() {
//        return serviceTitle;
//    }
//
//    public void setServiceTitle(String serviceTitle) {
//        this.serviceTitle = serviceTitle;
//    }
//
//    public String getServiceDescription() {
//        return serviceDescription;
//    }
//
//    public void setServiceDescription(String serviceDescription) {
//        this.serviceDescription = serviceDescription;
//    }
//
//    public String getImage1Url() {
//        return image1Url;
//    }
//
//    public void setImage1Url(String image1Url) {
//        this.image1Url = image1Url;
//    }
//
//    public String getDescription1() {
//        return description1;
//    }
//
//    public void setDescription1(String description1) {
//        this.description1 = description1;
//    }
//
//    public String getImage2Url() {
//        return image2Url;
//    }
//
//    public void setImage2Url(String image2Url) {
//        this.image2Url = image2Url;
//    }
//
//    public String getDescription2() {
//        return description2;
//    }
//
//    public void setDescription2(String description2) {
//        this.description2 = description2;
//    }
//
//    public String getImage3Url() {
//        return image3Url;
//    }
//
//    public void setImage3Url(String image3Url) {
//        this.image3Url = image3Url;
//    }
//
//    public String getDescription3() {
//        return description3;
//    }
//
//    public void setDescription3(String description3) {
//        this.description3 = description3;
//    }
//
//    public String getImage4Url() {
//        return image4Url;
//    }
//
//    public void setImage4Url(String image4Url) {
//        this.image4Url = image4Url;
//    }
//
//    public String getDescription4() {
//        return description4;
//    }
//
//    public void setDescription4(String description4) {
//        this.description4 = description4;
//    }
}