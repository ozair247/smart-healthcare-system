package com.smarthealth.healthcarebackend.service.impl;

import com.smarthealth.healthcarebackend.dto.request.AppointmentRequest;
import com.smarthealth.healthcarebackend.dto.response.AppointmentResponse;
import com.smarthealth.healthcarebackend.entity.*;
import com.smarthealth.healthcarebackend.enums.AppointmentStatus;
import com.smarthealth.healthcarebackend.exception.ResourceNotFoundException;
import com.smarthealth.healthcarebackend.repository.*;
import com.smarthealth.healthcarebackend.service.AppointmentService;
import com.smarthealth.healthcarebackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private EmailService emailService;
    @Autowired private BillRepository billRepository;          // <-- NEW

    @Override
    public AppointmentResponse scheduleAppointment(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDateTime(request.getAppointmentDateTime())
                .reason(request.getReason())
                .status(AppointmentStatus.SCHEDULED)
                .notes(request.getNotes())
                .build();
        appointment = appointmentRepository.save(appointment);

        emailService.sendAppointmentReminder(patient.getUser().getEmail(), appointment);
        return mapToResponse(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long id) {
        Appointment app = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        return mapToResponse(app);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // NEW
    @Override
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        // Delete related bills
        List<Bill> bills = billRepository.findByAppointmentId(id);
        billRepository.deleteAll(bills);
        appointmentRepository.delete(appointment);
    }

    private AppointmentResponse mapToResponse(Appointment app) {
        AppointmentResponse res = new AppointmentResponse();
        res.setId(app.getId());
        res.setPatientName(app.getPatient().getUser().getFullName());
        res.setDoctorName(app.getDoctor().getUser().getFullName());
        res.setAppointmentDateTime(app.getAppointmentDateTime());
        res.setReason(app.getReason());
        res.setStatus(app.getStatus().name());
        return res;
    }
}