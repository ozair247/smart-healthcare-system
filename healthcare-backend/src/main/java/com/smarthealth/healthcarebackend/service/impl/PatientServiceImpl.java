package com.smarthealth.healthcarebackend.service.impl;

import com.smarthealth.healthcarebackend.dto.request.PatientRequest;
import com.smarthealth.healthcarebackend.dto.response.PatientResponse;
import com.smarthealth.healthcarebackend.entity.*;
import com.smarthealth.healthcarebackend.exception.ResourceNotFoundException;
import com.smarthealth.healthcarebackend.repository.*;
import com.smarthealth.healthcarebackend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {
    @Autowired private PatientRepository patientRepository;
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private MedicalRecordRepository medicalRecordRepository;
    @Autowired private BillRepository billRepository;

    @Override
    public PatientResponse getPatientByUserId(Long userId) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found for user id: " + userId));
        return mapToResponse(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return mapToResponse(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient.setAllergies(request.getAllergies());
        patient.getUser().setFullName(request.getFullName());
        patient.getUser().setPhone(request.getPhone());
        patient.getUser().setAddress(request.getAddress());
        patientRepository.save(patient);
        return mapToResponse(patient);
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        // Remove related appointments
        List<Appointment> appointments = appointmentRepository.findByPatientId(id);
        appointmentRepository.deleteAll(appointments);

        // Remove related medical records
        List<MedicalRecord> records = medicalRecordRepository.findByPatientId(id);
        medicalRecordRepository.deleteAll(records);

        // Remove related bills
        List<Bill> bills = billRepository.findByPatientId(id);
        billRepository.deleteAll(bills);

        // Delete the patient (cascade will remove User if set)
        patientRepository.delete(patient);
    }

    private PatientResponse mapToResponse(Patient patient) {
        PatientResponse res = new PatientResponse();
        res.setId(patient.getId());
        res.setUserId(patient.getUser().getId());
        res.setFullName(patient.getUser().getFullName());
        res.setEmail(patient.getUser().getEmail());
        res.setPhone(patient.getUser().getPhone());
        res.setDateOfBirth(patient.getDateOfBirth());
        res.setBloodGroup(patient.getBloodGroup());
        res.setMedicalHistory(patient.getMedicalHistory());
        return res;
    }
}