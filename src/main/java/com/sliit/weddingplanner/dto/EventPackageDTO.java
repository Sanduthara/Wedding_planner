package com.sliit.weddingplanner.dto;


import java.time.LocalDateTime;

public class EventPackageDTO {
    private Integer eventPackageId;
    private Integer eventId;
    private Integer packageId;
    private String packageTitle;
    private String categoryName;
    private java.math.BigDecimal packagePrice;
    private Integer quantity;
    private String notes;
    private LocalDateTime addedAt;
    public EventPackageDTO() {}

    public EventPackageDTO(Integer eventPackageId, Integer eventId, Integer packageId, String packageTitle, String categoryName, java.math.BigDecimal packagePrice, Integer quantity, String notes, LocalDateTime addedAt) {
        this.eventPackageId = eventPackageId;
        this.eventId = eventId;
        this.packageId = packageId;
        this.packageTitle = packageTitle;
        this.categoryName = categoryName;
        this.packagePrice = packagePrice;
        this.quantity = quantity;
        this.notes = notes;
        this.addedAt = addedAt;
    }

    public Integer getEventPackageId() { return eventPackageId; }
    public void setEventPackageId(Integer eventPackageId) { this.eventPackageId = eventPackageId; }
    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }
    public Integer getPackageId() { return packageId; }
    public void setPackageId(Integer packageId) { this.packageId = packageId; }
    public String getPackageTitle() { return packageTitle; }
    public void setPackageTitle(String packageTitle) { this.packageTitle = packageTitle; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public java.math.BigDecimal getPackagePrice() { return packagePrice; }
    public void setPackagePrice(java.math.BigDecimal packagePrice) { this.packagePrice = packagePrice; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}
