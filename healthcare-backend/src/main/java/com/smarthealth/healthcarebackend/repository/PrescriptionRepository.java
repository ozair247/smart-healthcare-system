package com.smarthealth.healthcarebackend.repository;

import com.smarthealth.healthcarebackend.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByMedicalRecordId(Long medicalRecordId);
}