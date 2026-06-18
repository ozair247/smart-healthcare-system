package com.smarthealth.healthcarebackend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MedicalRecordRequest {
    @NotNull
    private Long patientId;
    @NotNull
    private Long doctorId;
    private LocalDate visitDate;
    private String diagnosis;
    private String treatment;
    private String notes;
}