package com.example.backend.repository;

import com.example.backend.entity.PricingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PricingPlanRepository extends JpaRepository<PricingPlan, UUID> {
    List<PricingPlan> findByUserIdAndIsActiveTrueOrderBySortOrder(UUID userId);
    List<PricingPlan> findByUserIdAndIsPopularTrue(UUID userId);
    List<PricingPlan> findByCategory(String category);
    List<PricingPlan> findByBillingPeriod(String billingPeriod);
}
