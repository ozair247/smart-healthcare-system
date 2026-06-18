package com.smarthealth.healthcarebackend.controller;

import com.smarthealth.healthcarebackend.dto.request.BillingRequest;
import com.smarthealth.healthcarebackend.dto.response.BillingResponse;
import com.smarthealth.healthcarebackend.service.BillingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingController {
    @Autowired
    private BillingService billingService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','PATIENT')")
    public ResponseEntity<List<BillingResponse>> getAllBills() {
        return ResponseEntity.ok(billingService.getAllBills());
    }

    @PostMapping
    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('ADMIN')")
    public ResponseEntity<BillingResponse> create(@Valid @RequestBody BillingRequest request) {
        return ResponseEntity.ok(billingService.createBill(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','PATIENT')")
    public ResponseEntity<BillingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.getBillById(id));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT','RECEPTIONIST','ADMIN')")
    public ResponseEntity<List<BillingResponse>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(billingService.getBillsByPatient(patientId));
    }

    @PutMapping("/{id}/pay")
    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('ADMIN')")
    public ResponseEntity<?> markAsPaid(@PathVariable Long id) {
        billingService.updatePaymentStatus(id, "PAID");
        return ResponseEntity.ok("Payment updated to PAID");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        billingService.deleteBill(id);
        return ResponseEntity.ok("Bill deleted");
    }
}