package com.sliit.weddingplanner.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

// OOP: Encapsulation
// OOP: Aggregation
// OOP: Interface-based Design
// Relationship: CompanyFinance tracks Payment and Vendor payouts
public class CompanyFinanceDTO {

    private int financeId;
    private String type; // INCOME or EXPENSE
    private Integer bookingId;
    private Integer paymentId;
    private BigDecimal amount;
    private String description;
    private String paymentMethod;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;
    public CompanyFinanceDTO() {}

    public CompanyFinanceDTO(int financeId, String type, Integer bookingId, Integer paymentId, BigDecimal amount, String description, String paymentMethod, LocalDateTime transactionDate, LocalDateTime createdAt) {
        this.financeId = financeId;
        this.type = type;
        this.bookingId = bookingId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.description = description;
        this.paymentMethod = paymentMethod;
        this.transactionDate = transactionDate;
        this.createdAt = createdAt;
    }

    public int getFinanceId() { return financeId; }
    public void setFinanceId(int financeId) { this.financeId = financeId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }
    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
