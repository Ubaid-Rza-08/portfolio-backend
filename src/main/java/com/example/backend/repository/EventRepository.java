package com.example.backend.repository;

import com.example.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByUserIdOrderByEventDateDesc(UUID userId);
    List<Event> findByUserIdAndIsFeaturedTrue(UUID userId);
    List<Event> findByEventType(String eventType);
    List<Event> findByStatus(String status);

    @Query("SELECT e FROM Event e WHERE e.userId = :userId AND e.eventDate >= :fromDate")
    List<Event> findUpcomingEventsByUserId(@Param("userId") UUID userId, @Param("fromDate") LocalDateTime fromDate);
}
