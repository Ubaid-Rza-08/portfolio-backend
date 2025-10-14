package com.example.backend.repository;

import com.example.backend.entity.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, UUID> {
    Optional<ContactInfo> findByUserId(UUID userId);
}
