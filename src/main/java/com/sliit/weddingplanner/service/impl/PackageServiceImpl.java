package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.dto.PackageDTO;
import com.sliit.weddingplanner.exception.ResourceNotFoundException;
import com.sliit.weddingplanner.repository.PackageRepository;
import com.sliit.weddingplanner.repository.VendorRepository;
import com.sliit.weddingplanner.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// OOP: Encapsulation
// OOP: Inheritance (Implements PackageService)
// OOP: Polymorphism
// Relationship: PackageServiceImpl implements PackageService
@Service
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final VendorRepository vendorRepository;

    @Autowired
    public PackageServiceImpl(PackageRepository packageRepository, VendorRepository vendorRepository) {
        this.packageRepository = packageRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public PackageDTO create(PackageDTO dto) {
        return packageRepository.save(dto);
    }

    @Override
    public PackageDTO getById(int id) {
        return packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id " + id));
    }

    @Override
    public List<PackageDTO> getAll() {
        return packageRepository.findAll();
    }

    @Override
    public PackageDTO update(int id, PackageDTO dto) {
        PackageDTO existing = getById(id);
        existing.setVendorId(dto.getVendorId());
        existing.setCategoryId(dto.getCategoryId());
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setDuration(dto.getDuration());
        existing.setAvailability(dto.getAvailability());
        return packageRepository.update(existing);
    }

    @Override
    public void delete(int id) {
        getById(id); // Ensure it exists
        packageRepository.delete(id);
    }

    @Override
    public List<PackageDTO> getPackagesByVendor(int vendorId) {
        return packageRepository.findAllByVendorId(vendorId);
    }

    @Override
    public List<PackageDTO> getAllAvailable() {
        return packageRepository.findAllAvailablePackages();
    }

    @Override
    public void updateAvailability(int id, String status) {
        PackageDTO existing = getById(id);
        existing.setAvailability(status);
        packageRepository.update(existing);
    }

    @Override
    public PackageDTO createForVendor(PackageDTO dto, int vendorId) {
        requireActiveVendor(vendorId);
        dto.setVendorId(vendorId);
        return packageRepository.save(dto);
    }

    @Override
    public PackageDTO updateForVendor(int id, PackageDTO dto, int vendorId) {
        requireActiveVendor(vendorId);
        PackageDTO existing = getById(id);
        requirePackageOwner(existing, vendorId);
        existing.setCategoryId(dto.getCategoryId());
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setDuration(dto.getDuration());
        existing.setAvailability(dto.getAvailability());
        return packageRepository.update(existing);
    }

    @Override
    public void deleteForVendor(int id, int vendorId) {
        requireActiveVendor(vendorId);
        PackageDTO existing = getById(id);
        requirePackageOwner(existing, vendorId);
        packageRepository.delete(id);
    }

    @Override
    public void updateAvailabilityForVendor(int id, String status, int vendorId) {
        if (!"AVAILABLE".equals(status) && !"UNAVAILABLE".equals(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid package availability");
        }
        requireActiveVendor(vendorId);
        PackageDTO existing = getById(id);
        requirePackageOwner(existing, vendorId);
        existing.setAvailability(status);
        packageRepository.update(existing);
    }

    private void requireActiveVendor(int vendorId) {
        var vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id " + vendorId));
        if (!"APPROVED".equals(vendor.getStatus()) || "DISABLED".equals(vendor.getAvailability())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only active, approved vendors can manage packages");
        }
    }

    private void requirePackageOwner(PackageDTO packageDTO, int vendorId) {
        if (!Integer.valueOf(vendorId).equals(packageDTO.getVendorId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this package");
        }
    }
}
