package com.smarthealth.healthcarebackend.controller;

import com.smarthealth.healthcarebackend.dto.request.PatientRequest;
import com.smarthealth.healthcarebackend.dto.response.PatientResponse;
import com.smarthealth.healthcarebackend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receptionist")
@PreAuthorize("hasRole('RECEPTIONIST')")
public class ReceptionistController {
    @Autowired private PatientService patientService;

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PutMapping("/patients/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }
}