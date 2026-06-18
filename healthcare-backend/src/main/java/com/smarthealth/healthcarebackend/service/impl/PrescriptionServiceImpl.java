package com.smarthealth.healthcarebackend.service.impl;

import com.smarthealth.healthcarebackend.dto.request.PrescriptionRequest;
import com.smarthealth.healthcarebackend.dto.response.PrescriptionResponse;
import com.smarthealth.healthcarebackend.entity.*;
import com.smarthealth.healthcarebackend.exception.ResourceNotFoundException;
import com.smarthealth.healthcarebackend.repository.*;
import com.smarthealth.healthcarebackend.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {
    @Autowired private PrescriptionRepository prescriptionRepository;
    @Autowired private MedicalRecordRepository medicalRecordRepository;

    @Override
    public PrescriptionResponse createPrescription(PrescriptionRequest request) {
        MedicalRecord record = medicalRecordRepository.findById(request.getMedicalRecordId())
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));
        Prescription prescription = Prescription.builder()
                .medicalRecord(record)
                .medicineName(request.getMedicineName())
                .dosage(request.getDosage())
                .duration(request.getDuration())
                .instructions(request.getInstructions())
                .build();
        prescription = prescriptionRepository.save(prescription);
        return mapToResponse(prescription);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getPrescriptionsByMedicalRecord(Long medicalRecordId) {
        return prescriptionRepository.findByMedicalRecordId(medicalRecordId).stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }

    // ✅ Added getAll endpoint (the frontend needs this)
    @Transactional(readOnly = true)
    @Override
    public List<PrescriptionResponse> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PrescriptionResponse mapToResponse(Prescription p) {
        PrescriptionResponse res = new PrescriptionResponse();
        res.setId(p.getId());
        res.setMedicineName(p.getMedicineName());
        res.setDosage(p.getDosage());
        res.setDuration(p.getDuration());
        return res;
    }
}