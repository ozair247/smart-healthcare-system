package com.smarthealth.healthcarebackend.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DoctorResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String specialization;
    private boolean available;
}