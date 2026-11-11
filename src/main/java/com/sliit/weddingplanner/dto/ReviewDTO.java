package com.sliit.weddingplanner.dto;


import java.time.LocalDateTime;

// OOP: Encapsulation
// OOP: Association
// OOP: Polymorphism
// Relationship: Review depends on Booking, Customer, Vendor, and Package
public class ReviewDTO {

    private Integer reviewId;
    private Integer eventId;
    private Integer customerId;
    private Integer packageId;
    private Integer packageRating;
    private String packageComment;
    private String customerName;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
    public ReviewDTO() {}

    public ReviewDTO(Integer reviewId, Integer eventId, Integer customerId, Integer packageId, Integer packageRating, String packageComment, String customerName, LocalDateTime createdAt, LocalDateTime reviewedAt) {
        this.reviewId = reviewId;
        this.eventId = eventId;
        this.customerId = customerId;
        this.packageId = packageId;
        this.packageRating = packageRating;
        this.packageComment = packageComment;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.reviewedAt = reviewedAt;
    }

    public Integer getReviewId() { return reviewId; }
    public void setReviewId(Integer reviewId) { this.reviewId = reviewId; }
    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public Integer getPackageId() { return packageId; }
    public void setPackageId(Integer packageId) { this.packageId = packageId; }
    public Integer getPackageRating() { return packageRating; }
    public void setPackageRating(Integer packageRating) { this.packageRating = packageRating; }
    public String getPackageComment() { return packageComment; }
    public void setPackageComment(String packageComment) { this.packageComment = packageComment; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
}
