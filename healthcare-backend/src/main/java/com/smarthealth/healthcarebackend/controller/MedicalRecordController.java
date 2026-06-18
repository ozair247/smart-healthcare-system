package com.smarthealth.healthcarebackend.controller;

import com.smarthealth.healthcarebackend.dto.request.MedicalRecordRequest;
import com.smarthealth.healthcarebackend.dto.response.MedicalRecordResponse;
import com.smarthealth.healthcarebackend.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {
    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR','PATIENT')")
    public ResponseEntity<List<MedicalRecordResponse>> getAllMedicalRecords() {
        return ResponseEntity.ok(medicalRecordService.getAllMedicalRecords());
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecordResponse> create(@Valid @RequestBody MedicalRecordRequest request) {
        return ResponseEntity.ok(medicalRecordService.createMedicalRecord(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT','RECEPTIONIST','ADMIN')")
    public ResponseEntity<MedicalRecordResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordById(id));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT','ADMIN')")
    public ResponseEntity<List<MedicalRecordResponse>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordsByPatient(patientId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.ok("Medical record deleted");
    }
}