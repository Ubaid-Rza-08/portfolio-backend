package com.example.backend.controller;

import com.example.backend.entity.ContactInfo;
import com.example.backend.service.ContactInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/contact-info")
@CrossOrigin(origins = "*")
public class ContactInfoController {

    @Autowired
    private ContactInfoService contactInfoService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ContactInfo> getContactInfoByUserId(@PathVariable UUID userId) {
        Optional<ContactInfo> contactInfo = contactInfoService.findByUserId(userId);
        return contactInfo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactInfo> getContactInfoById(@PathVariable UUID id) {
        Optional<ContactInfo> contactInfo = contactInfoService.findById(id);
        return contactInfo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ContactInfo> createContactInfo(@RequestBody ContactInfo contactInfo) {
        try {
            ContactInfo savedContactInfo = contactInfoService.saveContactInfo(contactInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedContactInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactInfo> updateContactInfo(@PathVariable UUID id, @RequestBody ContactInfo contactInfoDetails) {
        try {
            ContactInfo updatedContactInfo = contactInfoService.updateContactInfo(id, contactInfoDetails);
            return ResponseEntity.ok(updatedContactInfo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactInfo(@PathVariable UUID id) {
        if (contactInfoService.deleteContactInfo(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}