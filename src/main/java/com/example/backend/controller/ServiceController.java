package com.example.backend.controller;

import com.example.backend.entity.Service;
import com.example.backend.service.ServiceService;
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
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Service>> getServicesByUserId(@PathVariable UUID userId) {
        List<Service> services = serviceService.findByUserId(userId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<Service>> getActiveServicesByUserId(@PathVariable UUID userId) {
        List<Service> services = serviceService.findActiveServicesByUserId(userId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable UUID id) {
        Optional<Service> service = serviceService.findById(id);
        return service.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Service>> getServicesByCategory(@PathVariable String category) {
        List<Service> services = serviceService.findByCategory(category);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<Service>> searchServices(@PathVariable UUID userId, @RequestParam String title) {
        List<Service> services = serviceService.searchByTitle(userId, title);
        return ResponseEntity.ok(services);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createService(
            @RequestParam UUID userId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String duration,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String features,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Integer sortOrder,
            @RequestParam(required = false) String serviceType,
            @RequestParam(required = false) Boolean consultationRequired,
            @RequestParam(required = false) MultipartFile image) {

        try {
            Service service = new Service();
            service.setUserId(userId);
            service.setTitle(title);
            service.setDescription(description);
            service.setDuration(duration);
            service.setCategory(category);
            service.setFeatures(features);
            service.setIsActive(isActive != null ? isActive : true);
            service.setSortOrder(sortOrder != null ? sortOrder : 0);
            service.setServiceType(serviceType);
            service.setConsultationRequired(consultationRequired != null ? consultationRequired : false);

            // Parse price
            if (price != null && !price.trim().isEmpty()) {
                service.setPrice(new BigDecimal(price));
            }

            // Save service first
            Service savedService = serviceService.saveService(service);

            // Handle image upload
            if (image != null && !image.isEmpty()) {
                String imageUrl = cloudinaryService.uploadServiceImage(image, savedService.getId());
                savedService.setImageUrl(imageUrl);
                savedService = serviceService.saveService(savedService);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedService);

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
    public ResponseEntity<Service> updateService(@PathVariable UUID id, @RequestBody Service serviceDetails) {
        try {
            Service updatedService = serviceService.updateService(id, serviceDetails);
            return ResponseEntity.ok(updatedService);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadServiceImage(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        try {
            Optional<Service> serviceOpt = serviceService.findById(id);
            if (serviceOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Service service = serviceOpt.get();

            // Delete old image if exists
            if (service.getImageUrl() != null) {
                cloudinaryService.deleteImage(service.getImageUrl());
            }

            // Upload new image
            String imageUrl = cloudinaryService.uploadServiceImage(file, id);
            service.setImageUrl(imageUrl);
            Service updatedService = serviceService.saveService(service);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Service image uploaded successfully");
            response.put("imageUrl", imageUrl);
            response.put("service", updatedService);

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable UUID id) {
        try {
            Optional<Service> serviceOpt = serviceService.findById(id);
            if (serviceOpt.isPresent()) {
                Service service = serviceOpt.get();

                // Delete image from Cloudinary
                if (service.getImageUrl() != null) {
                    cloudinaryService.deleteImage(service.getImageUrl());
                }

                if (serviceService.deleteService(id)) {
                    return ResponseEntity.noContent().build();
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getServiceCount() {
        long count = serviceService.getServicesCount();
        return ResponseEntity.ok(count);
    }
}

