package com.smarthealth.healthcarebackend.service;

import com.smarthealth.healthcarebackend.dto.request.AppointmentRequest;
import com.smarthealth.healthcarebackend.dto.response.AppointmentResponse;
import java.util.List;

public interface AppointmentService {
    AppointmentResponse scheduleAppointment(AppointmentRequest request);
    AppointmentResponse getAppointmentById(Long id);
    List<AppointmentResponse> getAppointmentsByPatient(Long patientId);
    List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId);
    void cancelAppointment(Long id);
    List<AppointmentResponse> getAllAppointments();
    void deleteAppointment(Long id);   // <-- NEW
}