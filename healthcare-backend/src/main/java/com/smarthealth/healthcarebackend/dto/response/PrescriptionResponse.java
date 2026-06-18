package com.smarthealth.healthcarebackend.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PrescriptionResponse {
    private Long id;
    private String medicineName;
    private String dosage;
    private String duration;
}