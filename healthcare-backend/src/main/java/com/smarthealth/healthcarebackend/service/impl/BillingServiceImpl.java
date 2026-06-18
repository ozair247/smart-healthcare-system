package com.smarthealth.healthcarebackend.service.impl;

import com.smarthealth.healthcarebackend.dto.request.BillingRequest;
import com.smarthealth.healthcarebackend.dto.response.BillingResponse;
import com.smarthealth.healthcarebackend.entity.*;
import com.smarthealth.healthcarebackend.enums.PaymentStatus;
import com.smarthealth.healthcarebackend.exception.ResourceNotFoundException;
import com.smarthealth.healthcarebackend.repository.*;
import com.smarthealth.healthcarebackend.service.BillingService;
import com.smarthealth.healthcarebackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional          // <-- FIX
public class BillingServiceImpl implements BillingService {
    @Autowired private BillRepository billRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private EmailService emailService;

    @Override
    public BillingResponse createBill(BillingRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            appointment = appointmentRepository.findById(request.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        }
        Bill bill = Bill.builder()
                .patient(patient)
                .appointment(appointment)
                .amount(request.getAmount())
                .paymentStatus(PaymentStatus.PENDING)
                .billingDate(LocalDate.now())
                .description(request.getDescription())
                .build();
        bill = billRepository.save(bill);
        emailService.sendBillNotification(patient.getUser().getEmail(), bill.getAmount());
        return mapToResponse(bill);
    }

    @Override
    @Transactional(readOnly = true)
    public BillingResponse getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));
        return mapToResponse(bill);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillingResponse> getBillsByPatient(Long patientId) {
        return billRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public void updatePaymentStatus(Long id, String status) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));
        bill.setPaymentStatus(PaymentStatus.valueOf(status.toUpperCase()));
        billRepository.save(bill);
    }

    @Override
    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BillingResponse> getAllBills() {
        return billRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private BillingResponse mapToResponse(Bill bill) {
        BillingResponse res = new BillingResponse();
        res.setId(bill.getId());
        res.setPatientName(bill.getPatient().getUser().getFullName());
        res.setAmount(bill.getAmount());
        res.setPaymentStatus(bill.getPaymentStatus().name());
        res.setBillingDate(bill.getBillingDate());
        return res;
    }
}