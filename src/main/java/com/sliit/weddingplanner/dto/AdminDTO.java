package com.sliit.weddingplanner.dto;


// OOP: Inheritance
// OOP: Encapsulation
// OOP: Polymorphism
// Relationship: Admin inherits User
public class AdminDTO extends UserDTO {

    private String role;
    public AdminDTO() {}

    public AdminDTO(String role) {
        this.role = role;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
