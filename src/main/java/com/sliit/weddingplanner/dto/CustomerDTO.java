package com.sliit.weddingplanner.dto;


// OOP: Inheritance
// OOP: Encapsulation
// OOP: Polymorphism
// Relationship: Customer inherits User
// Relationship: Customer associates with Event
// Relationship: Customer associates with Booking
public class CustomerDTO extends UserDTO {

    private String title;
    private String customerRole;
    private String otherPartyName;
    private String phone;
    public CustomerDTO() {}

    public CustomerDTO(String title, String customerRole, String otherPartyName, String phone) {
        this.title = title;
        this.customerRole = customerRole;
        this.otherPartyName = otherPartyName;
        this.phone = phone;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCustomerRole() { return customerRole; }
    public void setCustomerRole(String customerRole) { this.customerRole = customerRole; }
    public String getOtherPartyName() { return otherPartyName; }
    public void setOtherPartyName(String otherPartyName) { this.otherPartyName = otherPartyName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
