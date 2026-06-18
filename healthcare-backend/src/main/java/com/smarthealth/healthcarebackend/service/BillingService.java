package com.smarthealth.healthcarebackend.service;

import com.smarthealth.healthcarebackend.dto.request.BillingRequest;
import com.smarthealth.healthcarebackend.dto.response.BillingResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BillingService {
    BillingResponse createBill(BillingRequest request);
    BillingResponse getBillById(Long id);
    List<BillingResponse> getBillsByPatient(Long patientId);
    void updatePaymentStatus(Long id, String status);
    void deleteBill(Long id);

    @Transactional(readOnly = true)
    List<BillingResponse> getAllBills();
}