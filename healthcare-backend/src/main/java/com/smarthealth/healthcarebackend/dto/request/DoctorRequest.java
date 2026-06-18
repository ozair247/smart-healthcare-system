package com.smarthealth.healthcarebackend.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DoctorRequest {
    private String specialization;
    private String licenseNumber;
    private String experienceYears;
    private Boolean available;
    private String fullName;
    private String phone;
    private String address;
}