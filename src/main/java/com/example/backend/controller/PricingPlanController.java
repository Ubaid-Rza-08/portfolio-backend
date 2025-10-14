package com.example.backend.controller;

import com.example.backend.entity.PricingPlan;
import com.example.backend.service.PricingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/pricing-plans")
@CrossOrigin(origins = "*")
public class PricingPlanController {

    @Autowired
    private PricingPlanService pricingPlanService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PricingPlan>> getPricingPlansByUserId(@PathVariable UUID userId) {
        List<PricingPlan> pricingPlans = pricingPlanService.findActiveByUserId(userId);
        return ResponseEntity.ok(pricingPlans);
    }

    @GetMapping("/user/{userId}/popular")
    public ResponseEntity<List<PricingPlan>> getPopularPlans(@PathVariable UUID userId) {
        List<PricingPlan> pricingPlans = pricingPlanService.findPopularByUserId(userId);
        return ResponseEntity.ok(pricingPlans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PricingPlan> getPricingPlanById(@PathVariable UUID id) {
        Optional<PricingPlan> pricingPlan = pricingPlanService.findById(id);
        return pricingPlan.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PricingPlan> createPricingPlan(@RequestBody PricingPlan pricingPlan) {
        try {
            PricingPlan savedPricingPlan = pricingPlanService.savePricingPlan(pricingPlan);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPricingPlan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PricingPlan> updatePricingPlan(@PathVariable UUID id, @RequestBody PricingPlan pricingPlanDetails) {
        try {
            PricingPlan updatedPricingPlan = pricingPlanService.updatePricingPlan(id, pricingPlanDetails);
            return ResponseEntity.ok(updatedPricingPlan);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePricingPlan(@PathVariable UUID id) {
        if (pricingPlanService.deletePricingPlan(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

