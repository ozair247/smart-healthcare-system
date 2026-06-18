package com.smarthealth.healthcarebackend.service.impl;

import com.smarthealth.healthcarebackend.dto.request.MedicalRecordRequest;
import com.smarthealth.healthcarebackend.dto.response.MedicalRecordResponse;
import com.smarthealth.healthcarebackend.entity.*;
import com.smarthealth.healthcarebackend.exception.ResourceNotFoundException;
import com.smarthealth.healthcarebackend.repository.*;
import com.smarthealth.healthcarebackend.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {
    @Autowired private MedicalRecordRepository medicalRecordRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private DoctorRepository doctorRepository;

    @Override
    public MedicalRecordResponse createMedicalRecord(MedicalRecordRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        MedicalRecord record = MedicalRecord.builder()
                .patient(patient).doctor(doctor)
                .visitDate(request.getVisitDate())
                .diagnosis(request.getDiagnosis())
                .treatment(request.getTreatment())
                .notes(request.getNotes())
                .build();
        record = medicalRecordRepository.save(record);
        return mapToResponse(record);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecordResponse getMedicalRecordById(Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));
        return mapToResponse(record);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> getMedicalRecordsByPatient(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        medicalRecordRepository.deleteById(id);
    }

    // ✅ Added getAll endpoint (the frontend needs this)
    @Transactional(readOnly = true)
    @Override
    public List<MedicalRecordResponse> getAllMedicalRecords() {
        return medicalRecordRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MedicalRecordResponse mapToResponse(MedicalRecord record) {
        MedicalRecordResponse res = new MedicalRecordResponse();
        res.setId(record.getId());
        res.setPatientName(record.getPatient().getUser().getFullName());
        res.setDoctorName(record.getDoctor().getUser().getFullName());
        res.setVisitDate(record.getVisitDate());
        res.setDiagnosis(record.getDiagnosis());
        res.setTreatment(record.getTreatment());
        return res;
    }
}