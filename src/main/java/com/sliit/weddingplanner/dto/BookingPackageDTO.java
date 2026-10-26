package com.sliit.weddingplanner.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingPackageDTO {
    private Integer bookingPackageId;
    private Integer bookingId;
    private Integer eventPackageId;
    private Integer quantity;
    private BigDecimal priceAtBooking;
    private String packageTitle;
    private String vendorName;
    private String vendorStatus;
    private String customerName;
    private String eventStatus;
    private java.time.LocalDate eventDate;
    private String location;
    private LocalDateTime vendorRespondedAt;
    private String rejectionReason;
    private String notes;
    private LocalDateTime createdAt;
    public BookingPackageDTO() {}

    public BookingPackageDTO(Integer bookingPackageId, Integer bookingId, Integer eventPackageId, Integer quantity, BigDecimal priceAtBooking, String packageTitle, String vendorName, String vendorStatus, String customerName, String eventStatus, java.time.LocalDate eventDate, String location, LocalDateTime vendorRespondedAt, String rejectionReason, String notes, LocalDateTime createdAt) {
        this.bookingPackageId = bookingPackageId;
        this.bookingId = bookingId;
        this.eventPackageId = eventPackageId;
        this.quantity = quantity;
        this.priceAtBooking = priceAtBooking;
        this.packageTitle = packageTitle;
        this.vendorName = vendorName;
        this.vendorStatus = vendorStatus;
        this.customerName = customerName;
        this.eventStatus = eventStatus;
        this.eventDate = eventDate;
        this.location = location;
        this.vendorRespondedAt = vendorRespondedAt;
        this.rejectionReason = rejectionReason;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Integer getBookingPackageId() { return bookingPackageId; }
    public void setBookingPackageId(Integer bookingPackageId) { this.bookingPackageId = bookingPackageId; }
    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }
    public Integer getEventPackageId() { return eventPackageId; }
    public void setEventPackageId(Integer eventPackageId) { this.eventPackageId = eventPackageId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getPriceAtBooking() { return priceAtBooking; }
    public void setPriceAtBooking(BigDecimal priceAtBooking) { this.priceAtBooking = priceAtBooking; }
    public String getPackageTitle() { return packageTitle; }
    public void setPackageTitle(String packageTitle) { this.packageTitle = packageTitle; }
    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }
    public String getVendorStatus() { return vendorStatus; }
    public void setVendorStatus(String vendorStatus) { this.vendorStatus = vendorStatus; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getEventStatus() { return eventStatus; }
    public void setEventStatus(String eventStatus) { this.eventStatus = eventStatus; }
    public java.time.LocalDate getEventDate() { return eventDate; }
    public void setEventDate(java.time.LocalDate eventDate) { this.eventDate = eventDate; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDateTime getVendorRespondedAt() { return vendorRespondedAt; }
    public void setVendorRespondedAt(LocalDateTime vendorRespondedAt) { this.vendorRespondedAt = vendorRespondedAt; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
