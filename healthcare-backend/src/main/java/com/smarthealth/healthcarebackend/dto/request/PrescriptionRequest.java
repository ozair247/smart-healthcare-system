package com.smarthealth.healthcarebackend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PrescriptionRequest {
    @NotNull
    private Long medicalRecordId;
    private String medicineName;
    private String dosage;
    private String duration;
    private String instructions;
}