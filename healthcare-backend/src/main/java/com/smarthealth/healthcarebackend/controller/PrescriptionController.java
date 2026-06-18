package com.smarthealth.healthcarebackend.controller;

import com.smarthealth.healthcarebackend.dto.request.PrescriptionRequest;
import com.smarthealth.healthcarebackend.dto.response.PrescriptionResponse;
import com.smarthealth.healthcarebackend.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    @Autowired
    private PrescriptionService prescriptionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    public ResponseEntity<List<PrescriptionResponse>> getAllPrescriptions() {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions());
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<PrescriptionResponse> create(@Valid @RequestBody PrescriptionRequest request) {
        return ResponseEntity.ok(prescriptionService.createPrescription(request));
    }

    @GetMapping("/medical-record/{medicalRecordId}")
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT','ADMIN')")
    public ResponseEntity<List<PrescriptionResponse>> getByMedicalRecord(@PathVariable Long medicalRecordId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByMedicalRecord(medicalRecordId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.ok("Prescription deleted");
    }
}