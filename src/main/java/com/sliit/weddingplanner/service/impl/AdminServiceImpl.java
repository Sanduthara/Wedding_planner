package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.dto.AdminDTO;
import com.sliit.weddingplanner.exception.DuplicateRecordException;
import com.sliit.weddingplanner.exception.ResourceNotFoundException;
import com.sliit.weddingplanner.repository.AdminRepository;
import com.sliit.weddingplanner.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// OOP: Encapsulation
// OOP: Inheritance (Implements AdminService)
// OOP: Polymorphism
// Relationship: AdminServiceImpl implements AdminService
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public AdminDTO createUser(AdminDTO adminDTO) {
        if (adminRepository.existsByUsernameOrEmail(adminDTO.getUsername(), adminDTO.getEmail())) {
            throw new DuplicateRecordException("Username or email already exists");
        }
        // Password encoding removed
        adminDTO.setRole("ROLE_ADMIN");
        return adminRepository.save(adminDTO);
    }

    @Override
    public AdminDTO getUserById(int id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id " + id));
    }

    @Override
    public List<AdminDTO> getAllUsers() {
        return adminRepository.findAll();
    }

    @Override
    public AdminDTO updateUser(int id, AdminDTO adminDetails) {
        AdminDTO admin = getUserById(id);
        
        // Update fields except password and id
        admin.setName(adminDetails.getName());
        admin.setUsername(adminDetails.getUsername());
        admin.setEmail(adminDetails.getEmail());
        
        return adminRepository.update(admin);
    }

    @Override
    public void deleteUser(int id) {
        AdminDTO admin = getUserById(id);
        adminRepository.delete(admin.getId());
    }

    @Override
    public void approveVendor(int vendorId, int adminId) {
        // Implementation typically calls vendor service, 
        // but for tight coupling rules we could inject VendorRepository or do nothing here if vendorService handles it.
        // Actually, we should just let VendorService handle it. 
        // We will throw UnsupportedOperationException if we don't have access to VendorRepository here.
        throw new UnsupportedOperationException("Call VendorService to approve vendor");
    }

    @Override
    public void rejectVendor(int vendorId, int adminId) {
        throw new UnsupportedOperationException("Call VendorService to reject vendor");
    }
}
