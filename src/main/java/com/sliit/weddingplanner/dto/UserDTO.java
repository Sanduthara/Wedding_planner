package com.sliit.weddingplanner.dto;


import java.time.LocalDateTime;

// OOP: Abstraction
// OOP: Encapsulation
// OOP: Inheritance (Parent Class)
public class UserDTO {

    protected Integer id;
    protected String username;
    protected String name;
    protected String email;
    protected String password;
    protected String role;
    protected LocalDateTime createdAt;
    public UserDTO() {}

    public UserDTO(Integer id, String username, String name, String email, String password, String role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
