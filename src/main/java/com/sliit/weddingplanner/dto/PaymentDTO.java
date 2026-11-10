package com.sliit.weddingplanner.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

// OOP: Encapsulation
// OOP: Association
// OOP: Abstraction
// Relationship: Payment belongs to Booking
public class PaymentDTO {

    private Integer paymentId;
    private String eventName;
    private Integer bookingId;
    private BigDecimal totalAmount;
    private BigDecimal amount;
    private BigDecimal dueAmount;
    private String paymentType;
    private String status;
    private LocalDateTime paymentDate;
    public PaymentDTO() {}

    public PaymentDTO(Integer paymentId, String eventName, Integer bookingId, BigDecimal totalAmount, BigDecimal amount, BigDecimal dueAmount, String paymentType, String status, LocalDateTime paymentDate) {
        this.paymentId = paymentId;
        this.eventName = eventName;
        this.bookingId = bookingId;
        this.totalAmount = totalAmount;
        this.amount = amount;
        this.dueAmount = dueAmount;
        this.paymentType = paymentType;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getDueAmount() { return dueAmount; }
    public void setDueAmount(BigDecimal dueAmount) { this.dueAmount = dueAmount; }
    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
}
