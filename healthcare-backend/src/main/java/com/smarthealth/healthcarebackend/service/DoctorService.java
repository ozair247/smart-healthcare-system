package com.smarthealth.healthcarebackend.service;

import com.smarthealth.healthcarebackend.dto.request.DoctorRequest;
import com.smarthealth.healthcarebackend.dto.response.DoctorResponse;
import java.util.List;

public interface DoctorService {
    DoctorResponse getDoctorById(Long id);
    List<DoctorResponse> getAllDoctors();
    DoctorResponse updateDoctor(Long id, DoctorRequest request);
    void deleteDoctor(Long id);
}