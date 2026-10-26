package com.sliit.weddingplanner.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

// OOP: Encapsulation
// OOP: Composition
// OOP: Association
// Relationship: Package belongs to Vendor
// Relationship: Booking depends on Package
public class PackageDTO {

    private Integer packageId;
    private Integer vendorId;
    private Integer categoryId;
    private String title;
    private String description;
    private BigDecimal price;
    private String duration;
    private String availability;
    private String vendorName;
    private Double averageRating;
    private Integer ratingCount;
    private Integer bookingCount;
    private LocalDateTime createdAt;
    public PackageDTO() {}

    public PackageDTO(Integer packageId, Integer vendorId, Integer categoryId, String title, String description, BigDecimal price, String duration, String availability, String vendorName, Double averageRating, Integer ratingCount, Integer bookingCount, LocalDateTime createdAt) {
        this.packageId = packageId;
        this.vendorId = vendorId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.availability = availability;
        this.vendorName = vendorName;
        this.averageRating = averageRating;
        this.ratingCount = ratingCount;
        this.bookingCount = bookingCount;
        this.createdAt = createdAt;
    }

    public Integer getPackageId() { return packageId; }
    public void setPackageId(Integer packageId) { this.packageId = packageId; }
    public Integer getVendorId() { return vendorId; }
    public void setVendorId(Integer vendorId) { this.vendorId = vendorId; }
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }
    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    public Integer getRatingCount() { return ratingCount; }
    public void setRatingCount(Integer ratingCount) { this.ratingCount = ratingCount; }
    public Integer getBookingCount() { return bookingCount; }
    public void setBookingCount(Integer bookingCount) { this.bookingCount = bookingCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
