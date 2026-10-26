package com.sliit.weddingplanner.dto;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// OOP: Encapsulation
// OOP: Composition
// OOP: Dependency Injection
// Relationship: Booking composes Payment
// Relationship: Booking depends on Event, Customer, Vendor, and Package
public class BookingDTO {

    private Integer bookingId;
    private Integer eventId;
    private String eventName;
    private String eventStatus;
    private Integer customerId;
    private String customerName;
    private LocalDate bookingDate;
    private String location;
    private String status;
    private String paymentStatus;
    private Double totalCost;
    private String paymentType;
    private List<BookingPackageDTO> packages;
    private LocalDateTime createdAt;
    public BookingDTO() {}

    public BookingDTO(Integer bookingId, Integer eventId, String eventName, String eventStatus, Integer customerId, String customerName, LocalDate bookingDate, String location, String status, String paymentStatus, Double totalCost, String paymentType, List<BookingPackageDTO> packages, LocalDateTime createdAt) {
        this.bookingId = bookingId;
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventStatus = eventStatus;
        this.customerId = customerId;
        this.customerName = customerName;
        this.bookingDate = bookingDate;
        this.location = location;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.totalCost = totalCost;
        this.paymentType = paymentType;
        this.packages = packages;
        this.createdAt = createdAt;
    }

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }
    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public String getEventStatus() { return eventStatus; }
    public void setEventStatus(String eventStatus) { this.eventStatus = eventStatus; }
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }
    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
    public List<BookingPackageDTO> getPackages() { return packages; }
    public void setPackages(List<BookingPackageDTO> packages) { this.packages = packages; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
