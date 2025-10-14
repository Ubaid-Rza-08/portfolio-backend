package com.example.backend.service;

import com.example.backend.entity.Portfolio;
import com.example.backend.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public Portfolio savePortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public List<Portfolio> findByUserId(UUID userId) {
        return portfolioRepository.findByUserIdOrderBySortOrder(userId);
    }

    public List<Portfolio> findFeaturedByUserId(UUID userId) {
        return portfolioRepository.findByUserIdAndIsFeaturedTrue(userId);
    }

    public Optional<Portfolio> findById(UUID id) {
        return portfolioRepository.findById(id);
    }

    public List<Portfolio> findByCategory(String category) {
        return portfolioRepository.findByCategory(category);
    }

    public List<Portfolio> findByStatus(String status) {
        return portfolioRepository.findByStatus(status);
    }

    public List<Portfolio> searchByTitle(UUID userId, String title) {
        return portfolioRepository.findByUserIdAndTitleContainingIgnoreCase(userId, title);
    }

    public Portfolio updatePortfolio(UUID id, Portfolio details) {
        return portfolioRepository.findById(id)
                .map(portfolio -> {
                    portfolio.setTitle(details.getTitle());
                    portfolio.setDescription(details.getDescription());
                    portfolio.setCategory(details.getCategory());
                    portfolio.setClientName(details.getClientName());
                    portfolio.setProjectDate(details.getProjectDate());
                    portfolio.setProjectUrl(details.getProjectUrl());
                    portfolio.setGithubUrl(details.getGithubUrl());
                    portfolio.setTechnologies(details.getTechnologies());
                    portfolio.setIsFeatured(details.getIsFeatured());
                    portfolio.setSortOrder(details.getSortOrder());
                    portfolio.setStatus(details.getStatus());
                    portfolio.setCompletionPercentage(details.getCompletionPercentage());
                    portfolio.setTestimonial(details.getTestimonial());
                    portfolio.setTestimonialAuthor(details.getTestimonialAuthor());
                    portfolio.setVideoUrl(details.getVideoUrl());
                    portfolio.setTags(details.getTags());
                    if (details.getImageUrls() != null) {
                        portfolio.setImageUrls(details.getImageUrls());
                    }
                    return portfolioRepository.save(portfolio);
                })
                .orElseThrow(() -> new RuntimeException("Portfolio not found with id: " + id));
    }

    public boolean deletePortfolio(UUID id) {
        if (portfolioRepository.existsById(id)) {
            portfolioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long getPortfoliosCount() {
        return portfolioRepository.count();
    }
}
