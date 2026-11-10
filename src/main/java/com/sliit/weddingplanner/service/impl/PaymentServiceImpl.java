package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.dto.PaymentDTO;
import com.sliit.weddingplanner.exception.ResourceNotFoundException;
import com.sliit.weddingplanner.repository.PaymentRepository;
import com.sliit.weddingplanner.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// OOP: Encapsulation
// OOP: Inheritance (Implements PaymentService)
// OOP: Polymorphism
// Relationship: PaymentServiceImpl implements PaymentService
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final com.sliit.weddingplanner.repository.CompanyFinanceRepository companyFinanceRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, com.sliit.weddingplanner.repository.CompanyFinanceRepository companyFinanceRepository) {
        this.paymentRepository = paymentRepository;
        this.companyFinanceRepository = companyFinanceRepository;
    }

    @Override
    public PaymentDTO create(PaymentDTO dto) {
        return paymentRepository.save(dto);
    }

    @Override
    public PaymentDTO getById(int id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + id));
    }

    @Override
    public List<PaymentDTO> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    public PaymentDTO update(int id, PaymentDTO dto) {
        dto.setPaymentId(id);
        return paymentRepository.update(dto);
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public PaymentDTO getPaymentByBookingId(int bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking " + bookingId));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void updatePaymentStatus(int paymentId, String status, String paymentType) {
        if ("PAID".equalsIgnoreCase(status)) {
            PaymentDTO payment = getById(paymentId);
            
            // 1. Update Payment details
            payment.setStatus("PAID");
            payment.setAmount(payment.getTotalAmount());
            payment.setDueAmount(java.math.BigDecimal.ZERO);
            if (paymentType != null) payment.setPaymentType(paymentType);
            paymentRepository.update(payment);
            
            // 2. Record in Company Finance
            com.sliit.weddingplanner.dto.CompanyFinanceDTO finance = new com.sliit.weddingplanner.dto.CompanyFinanceDTO();
            finance.setType("INCOME");
            finance.setBookingId(payment.getBookingId());
            finance.setPaymentId(payment.getPaymentId());
            finance.setAmount(payment.getTotalAmount());
            finance.setDescription(payment.getPaymentType() + " payment for Booking #" + payment.getBookingId());
            finance.setPaymentMethod("ONLINE");
            companyFinanceRepository.save(finance);
            
            System.out.println("DEBUG: Finance record created for Payment #" + paymentId + " with type " + payment.getPaymentType());
        } else {
            paymentRepository.updateStatus(paymentId, status);
        }
    }

    @Override
    public List<PaymentDTO> getPaymentsByCustomerId(int customerId) {
        return paymentRepository.findAllByCustomerId(customerId);
    }
}
