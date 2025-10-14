package com.example.backend.repository;

import com.example.backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByUserIdOrderByAppointmentDateDesc(UUID userId);
    List<Appointment> findByStatus(String status);
    List<Appointment> findByClientEmail(String clientEmail);

    @Query("SELECT a FROM Appointment a WHERE a.userId = :userId AND a.appointmentDate >= :fromDate AND a.appointmentDate <= :toDate")
    List<Appointment> findByUserIdAndDateRange(@Param("userId") UUID userId,
                                               @Param("fromDate") LocalDateTime fromDate,
                                               @Param("toDate") LocalDateTime toDate);

    @Query("SELECT a FROM Appointment a WHERE a.userId = :userId AND a.appointmentDate >= :fromDate")
    List<Appointment> findUpcomingAppointmentsByUserId(@Param("userId") UUID userId, @Param("fromDate") LocalDateTime fromDate);
}
