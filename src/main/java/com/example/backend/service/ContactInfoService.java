package com.example.backend.service;


import com.example.backend.entity.ContactInfo;
import com.example.backend.repository.ContactInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ContactInfoService {

    @Autowired
    private ContactInfoRepository contactInfoRepository;

    public ContactInfo saveContactInfo(ContactInfo contactInfo) {
        return contactInfoRepository.save(contactInfo);
    }

    public Optional<ContactInfo> findByUserId(UUID userId) {
        return contactInfoRepository.findByUserId(userId);
    }

    public Optional<ContactInfo> findById(UUID id) {
        return contactInfoRepository.findById(id);
    }

    public ContactInfo updateContactInfo(UUID id, ContactInfo details) {
        return contactInfoRepository.findById(id)
                .map(contactInfo -> {
                    contactInfo.setEmail(details.getEmail());
                    contactInfo.setPhone(details.getPhone());
                    contactInfo.setAddress(details.getAddress());
                    contactInfo.setWebsite(details.getWebsite());
                    contactInfo.setSocialLinks(details.getSocialLinks());
                    contactInfo.setContactFormEnabled(details.getContactFormEnabled());
                    contactInfo.setPreferredContactMethod(details.getPreferredContactMethod());
                    contactInfo.setAvailabilityHours(details.getAvailabilityHours());
                    contactInfo.setTimeZone(details.getTimeZone());
                    return contactInfoRepository.save(contactInfo);
                })
                .orElseThrow(() -> new RuntimeException("ContactInfo not found with id: " + id));
    }

    public boolean deleteContactInfo(UUID id) {
        if (contactInfoRepository.existsById(id)) {
            contactInfoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
