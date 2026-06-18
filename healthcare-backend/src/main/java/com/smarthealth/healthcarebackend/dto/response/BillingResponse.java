package com.smarthealth.healthcarebackend.dto.response;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BillingResponse {
    private Long id;
    private String patientName;
    private double amount;
    private String paymentStatus;
    private LocalDate billingDate;
}