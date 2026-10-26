package com.sliit.weddingplanner.dto;


import java.time.LocalDate;
import java.time.LocalDateTime;

// OOP: Encapsulation
// OOP: Abstraction
// OOP: Association
// Relationship: Event belongs to Customer
// Relationship: Event associates with Booking
public class EventDTO {

    private Integer eventId;
    private Integer customerId;
    private String eventName;
    private String eventType;
    private LocalDate eventDate;
    private String location;
    private String description;
    private String status;
    private Integer eventRating;
    private String eventReview;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
    public EventDTO() {}

    public EventDTO(Integer eventId, Integer customerId, String eventName, String eventType, LocalDate eventDate, String location, String description, String status, Integer eventRating, String eventReview, LocalDateTime reviewedAt, LocalDateTime createdAt) {
        this.eventId = eventId;
        this.customerId = customerId;
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.location = location;
        this.description = description;
        this.status = status;
        this.eventRating = eventRating;
        this.eventReview = eventReview;
        this.reviewedAt = reviewedAt;
        this.createdAt = createdAt;
    }

    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getEventRating() { return eventRating; }
    public void setEventRating(Integer eventRating) { this.eventRating = eventRating; }
    public String getEventReview() { return eventReview; }
    public void setEventReview(String eventReview) { this.eventReview = eventReview; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
