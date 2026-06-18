package com.smarthealth.healthcarebackend.service;

import com.smarthealth.healthcarebackend.dto.request.PrescriptionRequest;
import com.smarthealth.healthcarebackend.dto.response.PrescriptionResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PrescriptionService {
    PrescriptionResponse createPrescription(PrescriptionRequest request);
    List<PrescriptionResponse> getPrescriptionsByMedicalRecord(Long medicalRecordId);
    void deletePrescription(Long id);

    // ✅ Added getAll endpoint (the frontend needs this)
    @Transactional(readOnly = true)
    List<PrescriptionResponse> getAllPrescriptions();
}