package com.example.backend.service;

import com.example.backend.entity.Service;
import com.example.backend.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
@Transactional
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public Service saveService(Service service) {
        return serviceRepository.save(service);
    }

    public List<Service> findByUserId(UUID userId) {
        return serviceRepository.findByUserIdOrderBySortOrder(userId);
    }

    public List<Service> findActiveServicesByUserId(UUID userId) {
        return serviceRepository.findByUserIdAndIsActiveTrue(userId);
    }

    public Optional<Service> findById(UUID id) {
        return serviceRepository.findById(id);
    }

    public List<Service> findByCategory(String category) {
        return serviceRepository.findByCategory(category);
    }

    public List<Service> searchByTitle(UUID userId, String title) {
        return serviceRepository.findByUserIdAndTitleContainingIgnoreCase(userId, title);
    }

    public Service updateService(UUID id, Service details) {
        return serviceRepository.findById(id)
                .map(service -> {
                    service.setTitle(details.getTitle());
                    service.setDescription(details.getDescription());
                    service.setPrice(details.getPrice());
                    service.setDuration(details.getDuration());
                    service.setCategory(details.getCategory());
                    service.setFeatures(details.getFeatures());
                    service.setIsActive(details.getIsActive());
                    service.setSortOrder(details.getSortOrder());
                    service.setServiceType(details.getServiceType());
                    service.setConsultationRequired(details.getConsultationRequired());
                    if (details.getImageUrl() != null) {
                        service.setImageUrl(details.getImageUrl());
                    }
                    return serviceRepository.save(service);
                })
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
    }

    public boolean deleteService(UUID id) {
        if (serviceRepository.existsById(id)) {
            serviceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long getServicesCount() {
        return serviceRepository.count();
    }
}

