package com.example.backend.service;


import com.example.backend.entity.FAQ;
import com.example.backend.repository.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class FAQService {

    @Autowired
    private FAQRepository faqRepository;

    public FAQ saveFAQ(FAQ faq) {
        return faqRepository.save(faq);
    }

    public List<FAQ> findPublishedByUserId(UUID userId) {
        return faqRepository.findByUserIdAndIsPublishedTrueOrderBySortOrder(userId);
    }

    public List<FAQ> findFeaturedByUserId(UUID userId) {
        return faqRepository.findByUserIdAndIsFeaturedTrue(userId);
    }

    public Optional<FAQ> findById(UUID id) {
        return faqRepository.findById(id);
    }

    public List<FAQ> findByCategory(String category) {
        return faqRepository.findByCategory(category);
    }

    public List<FAQ> searchByQuery(UUID userId, String query) {
        return faqRepository.searchByUserIdAndQuery(userId, query);
    }

    public FAQ updateFAQ(UUID id, FAQ details) {
        return faqRepository.findById(id)
                .map(faq -> {
                    faq.setQuestion(details.getQuestion());
                    faq.setAnswer(details.getAnswer());
                    faq.setCategory(details.getCategory());
                    faq.setIsFeatured(details.getIsFeatured());
                    faq.setSortOrder(details.getSortOrder());
                    faq.setIsPublished(details.getIsPublished());
                    faq.setTags(details.getTags());
                    return faqRepository.save(faq);
                })
                .orElseThrow(() -> new RuntimeException("FAQ not found with id: " + id));
    }

    public boolean deleteFAQ(UUID id) {
        if (faqRepository.existsById(id)) {
            faqRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
