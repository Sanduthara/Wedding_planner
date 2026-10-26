package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.dto.VendorDTO;
import com.sliit.weddingplanner.exception.DuplicateRecordException;
import com.sliit.weddingplanner.exception.ResourceNotFoundException;
import com.sliit.weddingplanner.repository.VendorRepository;
import com.sliit.weddingplanner.repository.PackageRepository;
import com.sliit.weddingplanner.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// OOP: Encapsulation
// OOP: Inheritance (Implements VendorService)
// OOP: Polymorphism
// Relationship: VendorServiceImpl implements VendorService
@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final PackageRepository packageRepository;

    @Autowired
    public VendorServiceImpl(VendorRepository vendorRepository, PackageRepository packageRepository) {
        this.vendorRepository = vendorRepository;
        this.packageRepository = packageRepository;
    }

    @Override
    public VendorDTO createUser(VendorDTO vendorDTO) {
        if (vendorRepository.existsByUsername(vendorDTO.getUsername())) {
            throw new DuplicateRecordException("Username already in use");
        }
        if (vendorRepository.existsByEmail(vendorDTO.getEmail())) {
            throw new DuplicateRecordException("Email already in use");
        }
        if (vendorRepository.existsByPhone(vendorDTO.getPhone())) {
            throw new DuplicateRecordException("Phone number already in use");
        }
        // Password encoding removed
        vendorDTO.setStatus("PENDING");
        return vendorRepository.save(vendorDTO);
    }

    @Override
    public VendorDTO getUserById(int id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id " + id));
    }

    @Override
    public List<VendorDTO> getAllUsers() {
        return vendorRepository.findAll();
    }

    @Override
    public VendorDTO updateUser(int id, VendorDTO user) {
        VendorDTO existing = getUserById(id);
        existing.setName(user.getName());
        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());
        existing.setPhone(user.getPhone());
        existing.setAvailability(user.getAvailability());
        existing.setStatus(user.getStatus());
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existing.setPassword(user.getPassword());
        }
        return vendorRepository.update(existing);
    }

    @Override
    public void deleteUser(int id) {
        getUserById(id); // Throws exception if not found
        vendorRepository.delete(id);
    }

    @Override
    public void approveVendor(int vendorId, int adminId) {
        vendorRepository.updateStatus(vendorId, "APPROVED", adminId);
    }

    @Override
    public void rejectVendor(int vendorId, int adminId) {
        vendorRepository.updateStatus(vendorId, "REJECTED", adminId);
    }

    @Override
    public void updateAvailability(int vendorId, String availability, Integer adminId) {
        VendorDTO current = getUserById(vendorId);
        
        // Restriction: If currently disabled by admin, only admin can change it back
        if ("DISABLED".equals(current.getAvailability()) && adminId == null) {
            throw new IllegalStateException("Account is disabled by administrator. Cannot change availability.");
        }

        vendorRepository.updateAvailability(vendorId, availability);

        // Cascading: If admin disables/enables vendor, sync packages
        if ("DISABLED".equals(availability)) {
            packageRepository.updateAvailabilityByVendorId(vendorId, "DISABLED");
        } else if (adminId != null && "AVAILABLE".equals(availability)) {
            // If admin re-enables, set packages to available
            packageRepository.updateAvailabilityByVendorId(vendorId, "AVAILABLE");
        }
    }
}
