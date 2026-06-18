package com.smarthealth.healthcarebackend.repository;

import com.smarthealth.healthcarebackend.entity.Receptionist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReceptionistRepository extends JpaRepository<Receptionist, Long> {
    Optional<Receptionist> findByUserId(Long userId);
}