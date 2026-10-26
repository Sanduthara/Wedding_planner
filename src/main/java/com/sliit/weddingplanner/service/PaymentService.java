package com.sliit.weddingplanner.service;

import com.sliit.weddingplanner.dto.PaymentDTO;

import java.util.List;

// OOP: Interface-based Design
// OOP: Abstraction
public interface PaymentService {
    PaymentDTO create(PaymentDTO dto);
    PaymentDTO getById(int id);
    List<PaymentDTO> getAll();
    PaymentDTO update(int id, PaymentDTO dto);
    void delete(int id);

    PaymentDTO getPaymentByBookingId(int bookingId);
    void updatePaymentStatus(int paymentId, String status, String paymentType);
    List<PaymentDTO> getPaymentsByCustomerId(int customerId);
}
