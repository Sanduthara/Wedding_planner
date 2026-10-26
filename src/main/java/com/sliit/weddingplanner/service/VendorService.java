package com.sliit.weddingplanner.service;

import com.sliit.weddingplanner.dto.VendorDTO;

// OOP: Interface-based Design
// OOP: Inheritance (Extends UserService)
// OOP: Abstraction
public interface VendorService extends UserService<VendorDTO> {
    void approveVendor(int vendorId, int adminId);
    void rejectVendor(int vendorId, int adminId);
    void updateAvailability(int vendorId, String availability, Integer adminId);
}
