package com.smarthealth.healthcarebackend.controller;

import com.smarthealth.healthcarebackend.dto.request.AppointmentRequest;
import com.smarthealth.healthcarebackend.dto.response.AppointmentResponse;
import com.smarthealth.healthcarebackend.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    @Autowired private AppointmentService appointmentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR','PATIENT')")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @PostMapping
    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponse> schedule(@Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.scheduleAppointment(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR','PATIENT')")
    public ResponseEntity<AppointmentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR','RECEPTIONIST','ADMIN')")
    public ResponseEntity<List<AppointmentResponse>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR','RECEPTIONIST','ADMIN')")
    public ResponseEntity<List<AppointmentResponse>> getByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('RECEPTIONIST','PATIENT')")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok("Appointment cancelled");
    }

    // NEW DELETE endpoint
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("Appointment deleted");
    }
}