package com.sliit.weddingplanner.controller;

import com.sliit.weddingplanner.dto.PackageDTO;
import com.sliit.weddingplanner.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: PackageController depends on PackageService
@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final PackageService packageService;

    @Autowired
    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping
    public ResponseEntity<PackageDTO> createPackage(@RequestBody PackageDTO packageDTO) {
        return new ResponseEntity<>(packageService.create(packageDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageDTO> getPackageById(@PathVariable int id) {
        return ResponseEntity.ok(packageService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PackageDTO>> getAllPackages() {
        return ResponseEntity.ok(packageService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackageDTO> updatePackage(@PathVariable int id, @RequestBody PackageDTO packageDTO) {
        return ResponseEntity.ok(packageService.update(id, packageDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable int id) {
        packageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<PackageDTO>> getAllAvailablePackages() {
        return ResponseEntity.ok(packageService.getAllAvailable());
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Void> updateAvailability(@PathVariable int id, @RequestParam String status) {
        packageService.updateAvailability(id, status);
        return ResponseEntity.ok().build();
    }
}
