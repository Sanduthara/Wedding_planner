package com.sliit.weddingplanner.dto;


import java.time.LocalDateTime;

// OOP: Inheritance
// OOP: Encapsulation
// OOP: Polymorphism
// Relationship: Vendor inherits User
// Relationship: Vendor aggregates Package
// Relationship: Vendor provides Booking
public class VendorDTO extends UserDTO {

    private String phone;
    
    private String availability;
    private String status;
    private Integer approvedBy;
    private LocalDateTime approvedAt;
    public VendorDTO() {}

    public VendorDTO(String phone, String availability, String status, Integer approvedBy, LocalDateTime approvedAt) {
        this.phone = phone;
        this.availability = availability;
        this.status = status;
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Integer approvedBy) { this.approvedBy = approvedBy; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
}
