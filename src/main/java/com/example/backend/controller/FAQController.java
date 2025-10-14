package com.example.backend.controller;

import com.example.backend.entity.FAQ;
import com.example.backend.service.FAQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/faqs")
@CrossOrigin(origins = "*")
public class FAQController {

    @Autowired
    private FAQService faqService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FAQ>> getFAQsByUserId(@PathVariable UUID userId) {
        List<FAQ> faqs = faqService.findPublishedByUserId(userId);
        return ResponseEntity.ok(faqs);
    }

    @GetMapping("/user/{userId}/featured")
    public ResponseEntity<List<FAQ>> getFeaturedFAQs(@PathVariable UUID userId) {
        List<FAQ> faqs = faqService.findFeaturedByUserId(userId);
        return ResponseEntity.ok(faqs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FAQ> getFAQById(@PathVariable UUID id) {
        Optional<FAQ> faq = faqService.findById(id);
        return faq.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<FAQ>> searchFAQs(@PathVariable UUID userId, @RequestParam String query) {
        List<FAQ> faqs = faqService.searchByQuery(userId, query);
        return ResponseEntity.ok(faqs);
    }

    @PostMapping
    public ResponseEntity<FAQ> createFAQ(@RequestBody FAQ faq) {
        try {
            FAQ savedFAQ = faqService.saveFAQ(faq);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFAQ);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FAQ> updateFAQ(@PathVariable UUID id, @RequestBody FAQ faqDetails) {
        try {
            FAQ updatedFAQ = faqService.updateFAQ(id, faqDetails);
            return ResponseEntity.ok(updatedFAQ);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFAQ(@PathVariable UUID id) {
        if (faqService.deleteFAQ(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
