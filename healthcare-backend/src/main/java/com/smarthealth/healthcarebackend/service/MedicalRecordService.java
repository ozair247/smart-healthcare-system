package com.smarthealth.healthcarebackend.service;

import com.smarthealth.healthcarebackend.dto.request.MedicalRecordRequest;
import com.smarthealth.healthcarebackend.dto.response.MedicalRecordResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MedicalRecordService {
    MedicalRecordResponse createMedicalRecord(MedicalRecordRequest request);
    MedicalRecordResponse getMedicalRecordById(Long id);
    List<MedicalRecordResponse> getMedicalRecordsByPatient(Long patientId);
    void deleteMedicalRecord(Long id);

    // ✅ Added getAll endpoint (the frontend needs this)
    @Transactional(readOnly = true)
    List<MedicalRecordResponse> getAllMedicalRecords();
}