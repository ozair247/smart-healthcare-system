package com.smarthealth.healthcarebackend.service.impl;

import com.smarthealth.healthcarebackend.dto.request.DoctorRequest;
import com.smarthealth.healthcarebackend.dto.response.DoctorResponse;
import com.smarthealth.healthcarebackend.entity.Doctor;
import com.smarthealth.healthcarebackend.entity.Appointment;
import com.smarthealth.healthcarebackend.exception.ResourceNotFoundException;
import com.smarthealth.healthcarebackend.repository.DoctorRepository;
import com.smarthealth.healthcarebackend.repository.AppointmentRepository;
import com.smarthealth.healthcarebackend.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private AppointmentRepository appointmentRepository;

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return mapToResponse(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        doctor.setSpecialization(request.getSpecialization());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setExperienceYears(request.getExperienceYears());
        if (request.getAvailable() != null) doctor.setAvailable(request.getAvailable());
        doctor.getUser().setFullName(request.getFullName());
        doctor.getUser().setPhone(request.getPhone());
        doctor.getUser().setAddress(request.getAddress());
        doctorRepository.save(doctor);
        return mapToResponse(doctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        // Remove appointments linked to this doctor
        List<Appointment> appointments = appointmentRepository.findByDoctorId(id);
        appointmentRepository.deleteAll(appointments);

        doctorRepository.delete(doctor);
    }

    private DoctorResponse mapToResponse(Doctor doctor) {
        DoctorResponse res = new DoctorResponse();
        res.setId(doctor.getId());
        res.setUserId(doctor.getUser().getId());
        res.setFullName(doctor.getUser().getFullName());
        res.setEmail(doctor.getUser().getEmail());
        res.setPhone(doctor.getUser().getPhone());
        res.setSpecialization(doctor.getSpecialization());
        res.setAvailable(doctor.isAvailable());
        return res;
    }
}