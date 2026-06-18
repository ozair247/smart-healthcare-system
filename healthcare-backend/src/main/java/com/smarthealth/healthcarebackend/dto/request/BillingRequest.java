package com.smarthealth.healthcarebackend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BillingRequest {
    @NotNull
    private Long patientId;
    private Long appointmentId;
    private double amount;
    private String description;
}