package com.smarthealth.healthcarebackend.entity;

import com.smarthealth.healthcarebackend.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private double amount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private LocalDate billingDate;
    private String description;
}