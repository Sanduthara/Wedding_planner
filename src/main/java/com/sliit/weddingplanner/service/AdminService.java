package com.sliit.weddingplanner.service;

import com.sliit.weddingplanner.dto.AdminDTO;

// OOP: Interface-based Design
// OOP: Inheritance (Extends UserService)
// OOP: Abstraction
public interface AdminService extends UserService<AdminDTO> {
    void approveVendor(int vendorId, int adminId);
    void rejectVendor(int vendorId, int adminId);
}
