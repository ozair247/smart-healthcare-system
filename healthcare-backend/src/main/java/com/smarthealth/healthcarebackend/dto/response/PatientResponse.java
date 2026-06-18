package com.smarthealth.healthcarebackend.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PatientResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String bloodGroup;
    private String medicalHistory;
}