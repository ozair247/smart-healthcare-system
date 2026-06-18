package com.smarthealth.healthcarebackend.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SignupRequest {
    @NotBlank @Size(min = 3, max = 20)
    private String username;
    @NotBlank @Size(max = 50) @Email
    private String email;
    @NotBlank @Size(min = 6, max = 40)
    private String password;
    @NotBlank
    private String role; // ADMIN, DOCTOR, RECEPTIONIST, PATIENT
    private String fullName;
    private String phone;
    private String address;
    // Patient specific
    private String dateOfBirth;
    private String bloodGroup;
    // Doctor specific
    private String specialization;
    private String licenseNumber;
    private String experienceYears;
    // Receptionist specific
    private String employeeId;
}