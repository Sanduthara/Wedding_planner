package com.sliit.weddingplanner.service;

import com.sliit.weddingplanner.dto.PackageDTO;

import java.util.List;

// OOP: Interface-based Design
// OOP: Abstraction
public interface PackageService {
    PackageDTO create(PackageDTO dto);
    PackageDTO getById(int id);
    List<PackageDTO> getAll();
    PackageDTO update(int id, PackageDTO dto);
    void delete(int id);

    List<PackageDTO> getPackagesByVendor(int vendorId);
    List<PackageDTO> getAllAvailable();
    void updateAvailability(int id, String status);
}
