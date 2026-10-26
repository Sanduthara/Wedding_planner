package com.sliit.weddingplanner.controller;

import com.sliit.weddingplanner.dto.PaymentDTO;
import com.sliit.weddingplanner.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: PaymentController depends on PaymentService
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO paymentDTO) {
        return new ResponseEntity<>(paymentService.create(paymentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable int id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentDTO> getPaymentByBookingId(@PathVariable int bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentByBookingId(bookingId));
    }

    @GetMapping
    public ResponseEntity<java.util.List<PaymentDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAll());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<java.util.List<PaymentDTO>> getPaymentsByCustomer(@PathVariable int customerId) {
        return ResponseEntity.ok(paymentService.getPaymentsByCustomerId(customerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updatePaymentStatus(
            @PathVariable int id, 
            @RequestParam String status,
            @RequestParam(required = false) String paymentType) {
        paymentService.updatePaymentStatus(id, status, paymentType);
        return ResponseEntity.ok().build();
    }
}
