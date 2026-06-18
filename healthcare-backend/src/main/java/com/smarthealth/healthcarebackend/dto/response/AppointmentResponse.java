package com.smarthealth.healthcarebackend.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private String patientName;
    private String doctorName;
    private LocalDateTime appointmentDateTime;
    private String reason;
    private String status;
}