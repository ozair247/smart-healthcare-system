package com.smarthealth.healthcarebackend.controller;

import com.smarthealth.healthcarebackend.dto.request.PatientRequest;
import com.smarthealth.healthcarebackend.dto.response.PatientResponse;
import com.smarthealth.healthcarebackend.entity.Patient;
import com.smarthealth.healthcarebackend.repository.PatientRepository;
import com.smarthealth.healthcarebackend.security.services.UserDetailsImpl;
import com.smarthealth.healthcarebackend.service.PatientService;
import com.smarthealth.healthcarebackend.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    @Autowired private PatientService patientService;
    @Autowired private PdfService pdfService;
    @Autowired private PatientRepository patientRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR','PATIENT')")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR')")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPTIONIST')")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok("Patient deleted");
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponse> getMyPatientProfile() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        return ResponseEntity.ok(patientService.getPatientByUserId(userId));
    }

    // PDF report endpoint
    @GetMapping("/{id}/report")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR')")
    public ResponseEntity<byte[]> getPatientReport(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        byte[] pdfBytes = pdfService.generatePatientReport(patient);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=patient-" + id + "-report.pdf")
                .body(pdfBytes);
    }
}