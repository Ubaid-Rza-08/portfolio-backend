package com.example.backend.controller;

import com.example.backend.service.DummyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dummy-data")
@CrossOrigin(origins = "*")
public class DummyDataController {

    @Autowired
    private DummyDataService dummyDataService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createDummyData() {
        try {
            dummyDataService.createCompleteDummyData();

            Map<String, String> response = new HashMap<>();
            response.put("message", "Dummy data created successfully!");
            response.put("status", "success");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create dummy data: " + e.getMessage());
            error.put("status", "error");

            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getDummyDataInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("description", "Complete portfolio dummy data including user profile, services, portfolios, testimonials, blogs, achievements, team members, events, pricing plans, FAQs, and appointments");
        info.put("endpoint", "/api/dummy-data/create");
        info.put("method", "POST");
        info.put("note", "This will create a comprehensive set of demo data for a portfolio website");

        return ResponseEntity.ok(info);
    }
}

