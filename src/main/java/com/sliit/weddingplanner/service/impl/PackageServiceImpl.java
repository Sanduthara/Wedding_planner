package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.dto.PackageDTO;
import com.sliit.weddingplanner.exception.ResourceNotFoundException;
import com.sliit.weddingplanner.repository.PackageRepository;
import com.sliit.weddingplanner.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// OOP: Encapsulation
// OOP: Inheritance (Implements PackageService)
// OOP: Polymorphism
// Relationship: PackageServiceImpl implements PackageService
@Service
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;

    @Autowired
    public PackageServiceImpl(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
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
}
