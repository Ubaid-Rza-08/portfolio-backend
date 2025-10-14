package com.example.backend.service;


import com.example.backend.entity.Event;
import com.example.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> findByUserId(UUID userId) {
        return eventRepository.findByUserIdOrderByEventDateDesc(userId);
    }

    public List<Event> findUpcomingByUserId(UUID userId) {
        return eventRepository.findUpcomingEventsByUserId(userId, LocalDateTime.now());
    }

    public List<Event> findFeaturedByUserId(UUID userId) {
        return eventRepository.findByUserIdAndIsFeaturedTrue(userId);
    }

    public Optional<Event> findById(UUID id) {
        return eventRepository.findById(id);
    }

    public List<Event> findByEventType(String eventType) {
        return eventRepository.findByEventType(eventType);
    }

    public List<Event> findByStatus(String status) {
        return eventRepository.findByStatus(status);
    }

    public Event updateEvent(UUID id, Event details) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setTitle(details.getTitle());
                    event.setDescription(details.getDescription());
                    event.setEventDate(details.getEventDate());
                    event.setEndDate(details.getEndDate());
                    event.setLocation(details.getLocation());
                    event.setVenue(details.getVenue());
                    event.setEventType(details.getEventType());
                    event.setEventUrl(details.getEventUrl());
                    event.setRegistrationUrl(details.getRegistrationUrl());
                    event.setIsOnline(details.getIsOnline());
                    event.setMaxAttendees(details.getMaxAttendees());
                    event.setCurrentAttendees(details.getCurrentAttendees());
                    event.setPrice(details.getPrice());
                    event.setCurrency(details.getCurrency());
                    event.setStatus(details.getStatus());
                    event.setTags(details.getTags());
                    event.setIsFeatured(details.getIsFeatured());
                    event.setOrganizerInfo(details.getOrganizerInfo());
                    event.setAgenda(details.getAgenda());
                    event.setRequirements(details.getRequirements());
                    event.setTargetAudience(details.getTargetAudience());
                    if (details.getImageUrl() != null) {
                        event.setImageUrl(details.getImageUrl());
                    }
                    return eventRepository.save(event);
                })
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    public boolean deleteEvent(UUID id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
