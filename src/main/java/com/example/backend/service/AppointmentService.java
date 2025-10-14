package com.example.backend.service;

import com.example.backend.entity.Appointment;
import com.example.backend.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findByUserId(UUID userId) {
        return appointmentRepository.findByUserIdOrderByAppointmentDateDesc(userId);
    }

    public List<Appointment> findUpcomingByUserId(UUID userId) {
        return appointmentRepository.findUpcomingAppointmentsByUserId(userId, LocalDateTime.now());
    }

    public Optional<Appointment> findById(UUID id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> findByStatus(String status) {
        return appointmentRepository.findByStatus(status);
    }

    public List<Appointment> findByClientEmail(String clientEmail) {
        return appointmentRepository.findByClientEmail(clientEmail);
    }

    public List<Appointment> findByDateRange(UUID userId, LocalDateTime fromDate, LocalDateTime toDate) {
        return appointmentRepository.findByUserIdAndDateRange(userId, fromDate, toDate);
    }

    public Appointment updateAppointment(UUID id, Appointment details) {
        return appointmentRepository.findById(id)
                .map(appointment -> {
                    appointment.setClientName(details.getClientName());
                    appointment.setClientEmail(details.getClientEmail());
                    appointment.setClientPhone(details.getClientPhone());
                    appointment.setAppointmentDate(details.getAppointmentDate());
                    appointment.setEndTime(details.getEndTime());
                    appointment.setServiceType(details.getServiceType());
                    appointment.setStatus(details.getStatus());
                    appointment.setNotes(details.getNotes());
                    appointment.setMeetingType(details.getMeetingType());
                    appointment.setMeetingUrl(details.getMeetingUrl());
                    appointment.setLocation(details.getLocation());
                    appointment.setDurationMinutes(details.getDurationMinutes());
                    appointment.setPrice(details.getPrice());
                    appointment.setIsPaid(details.getIsPaid());
                    appointment.setReminderSent(details.getReminderSent());
                    appointment.setBookingSource(details.getBookingSource());
                    appointment.setSpecialRequirements(details.getSpecialRequirements());
                    appointment.setCancellationReason(details.getCancellationReason());
                    appointment.setFollowUpRequired(details.getFollowUpRequired());
                    return appointmentRepository.save(appointment);
                })
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    }

    public boolean deleteAppointment(UUID id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
