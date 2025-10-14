package com.example.backend.controller;

import com.example.backend.entity.Event;
import com.example.backend.service.EventService;
import com.example.backend.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Event>> getEventsByUserId(@PathVariable UUID userId) {
        List<Event> events = eventService.findByUserId(userId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/user/{userId}/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents(@PathVariable UUID userId) {
        List<Event> events = eventService.findUpcomingByUserId(userId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/user/{userId}/featured")
    public ResponseEntity<List<Event>> getFeaturedEvents(@PathVariable UUID userId) {
        List<Event> events = eventService.findFeaturedByUserId(userId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable UUID id) {
        Optional<Event> event = eventService.findById(id);
        return event.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEvent(
            @RequestParam UUID userId,
            @RequestParam String title,
            @RequestParam String eventDate,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String venue,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String eventUrl,
            @RequestParam(required = false) String registrationUrl,
            @RequestParam(required = false) Boolean isOnline,
            @RequestParam(required = false) Integer maxAttendees,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) Boolean isFeatured,
            @RequestParam(required = false) String organizerInfo,
            @RequestParam(required = false) String agenda,
            @RequestParam(required = false) String requirements,
            @RequestParam(required = false) String targetAudience,
            @RequestParam(required = false) MultipartFile image) {

        try {
            Event event = new Event();
            event.setUserId(userId);
            event.setTitle(title);
            event.setDescription(description);
            event.setLocation(location);
            event.setVenue(venue);
            event.setEventType(eventType);
            event.setEventUrl(eventUrl);
            event.setRegistrationUrl(registrationUrl);
            event.setIsOnline(isOnline != null ? isOnline : false);
            event.setMaxAttendees(maxAttendees);
            event.setCurrency(currency != null ? currency : "USD");
            event.setStatus(status != null ? status : "upcoming");
            event.setTags(tags);
            event.setIsFeatured(isFeatured != null ? isFeatured : false);
            event.setOrganizerInfo(organizerInfo);
            event.setAgenda(agenda);
            event.setRequirements(requirements);
            event.setTargetAudience(targetAudience);

            // Parse dates
            event.setEventDate(LocalDateTime.parse(eventDate));
            if (endDate != null && !endDate.trim().isEmpty()) {
                event.setEndDate(LocalDateTime.parse(endDate));
            }

            // Parse price
            if (price != null && !price.trim().isEmpty()) {
                event.setPrice(new BigDecimal(price));
            }

            Event savedEvent = eventService.saveEvent(event);

            // Handle image upload
            if (image != null && !image.isEmpty()) {
                String imageUrl = cloudinaryService.uploadEventImage(image, savedEvent.getId());
                savedEvent.setImageUrl(imageUrl);
                savedEvent = eventService.saveEvent(savedEvent);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);

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

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable UUID id, @RequestBody Event eventDetails) {
        try {
            Event updatedEvent = eventService.updateEvent(id, eventDetails);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        try {
            Optional<Event> eventOpt = eventService.findById(id);
            if (eventOpt.isPresent()) {
                Event event = eventOpt.get();

                if (event.getImageUrl() != null) {
                    cloudinaryService.deleteImage(event.getImageUrl());
                }

                if (eventService.deleteEvent(id)) {
                    return ResponseEntity.noContent().build();
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
