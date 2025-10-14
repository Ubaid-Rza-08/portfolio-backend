package com.example.backend.service;

import com.example.backend.entity.PricingPlan;
import com.example.backend.repository.PricingPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PricingPlanService {

    @Autowired
    private PricingPlanRepository pricingPlanRepository;

    public PricingPlan savePricingPlan(PricingPlan pricingPlan) {
        return pricingPlanRepository.save(pricingPlan);
    }

    public List<PricingPlan> findActiveByUserId(UUID userId) {
        return pricingPlanRepository.findByUserIdAndIsActiveTrueOrderBySortOrder(userId);
    }

    public List<PricingPlan> findPopularByUserId(UUID userId) {
        return pricingPlanRepository.findByUserIdAndIsPopularTrue(userId);
    }

    public Optional<PricingPlan> findById(UUID id) {
        return pricingPlanRepository.findById(id);
    }

    public List<PricingPlan> findByCategory(String category) {
        return pricingPlanRepository.findByCategory(category);
    }

    public List<PricingPlan> findByBillingPeriod(String billingPeriod) {
        return pricingPlanRepository.findByBillingPeriod(billingPeriod);
    }

    public PricingPlan updatePricingPlan(UUID id, PricingPlan details) {
        return pricingPlanRepository.findById(id)
                .map(plan -> {
                    plan.setPlanName(details.getPlanName());
                    plan.setDescription(details.getDescription());
                    plan.setPrice(details.getPrice());
                    plan.setCurrency(details.getCurrency());
                    plan.setBillingPeriod(details.getBillingPeriod());
                    plan.setFeatures(details.getFeatures());
                    plan.setIsPopular(details.getIsPopular());
                    plan.setIsActive(details.getIsActive());
                    plan.setSortOrder(details.getSortOrder());
                    plan.setMaxRevisions(details.getMaxRevisions());
                    plan.setDeliveryTimeDays(details.getDeliveryTimeDays());
                    plan.setIncludesSourceFiles(details.getIncludesSourceFiles());
                    plan.setIncludesCommercialUse(details.getIncludesCommercialUse());
                    plan.setDiscountPercentage(details.getDiscountPercentage());
                    plan.setOriginalPrice(details.getOriginalPrice());
                    plan.setSetupFee(details.getSetupFee());
                    plan.setCategory(details.getCategory());
                    plan.setButtonText(details.getButtonText());
                    plan.setButtonUrl(details.getButtonUrl());
                    return pricingPlanRepository.save(plan);
                })
                .orElseThrow(() -> new RuntimeException("Pricing plan not found with id: " + id));
    }

    public boolean deletePricingPlan(UUID id) {
        if (pricingPlanRepository.existsById(id)) {
            pricingPlanRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

