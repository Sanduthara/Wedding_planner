package com.sliit.weddingplanner.dto;


import java.time.LocalDateTime;

// OOP: Encapsulation
public class CategoryDTO {

    private Integer categoryId;
    private String categoryName;
    private String description;
    private LocalDateTime createdAt;
    public CategoryDTO() {}

    public CategoryDTO(Integer categoryId, String categoryName, String description, LocalDateTime createdAt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
