package com.smarthealth.healthcarebackend.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PatientRequest {
    private String dateOfBirth;
    private String bloodGroup;
    private String medicalHistory;
    private String allergies;
    private String fullName;
    private String phone;
    private String address;
}