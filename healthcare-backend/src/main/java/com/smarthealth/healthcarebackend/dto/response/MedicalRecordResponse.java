package com.smarthealth.healthcarebackend.dto.response;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MedicalRecordResponse {
    private Long id;
    private String patientName;
    private String doctorName;
    private LocalDate visitDate;
    private String diagnosis;
    private String treatment;
}