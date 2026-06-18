package com.smarthealth.healthcarebackend.service;

import com.smarthealth.healthcarebackend.dto.request.PatientRequest;
import com.smarthealth.healthcarebackend.dto.response.PatientResponse;
import java.util.List;

public interface PatientService {
    PatientResponse getPatientByUserId(Long userId);
    PatientResponse getPatientById(Long id);
    List<PatientResponse> getAllPatients();
    PatientResponse updatePatient(Long id, PatientRequest request);
    void deletePatient(Long id);
}