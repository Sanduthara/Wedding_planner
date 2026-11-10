package com.sliit.weddingplanner.controller;

import com.sliit.weddingplanner.dto.PackageDTO;
import com.sliit.weddingplanner.security.AuthTokenService;
import com.sliit.weddingplanner.security.AuthenticatedUser;
import com.sliit.weddingplanner.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: PackageController depends on PackageService
@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final PackageService packageService;
    private final AuthTokenService authTokenService;

    @Autowired
    public PackageController(PackageService packageService, AuthTokenService authTokenService) {
        this.packageService = packageService;
        this.authTokenService = authTokenService;
    }

    @PostMapping
    public ResponseEntity<PackageDTO> createPackage(
            @RequestBody PackageDTO packageDTO,
            HttpServletRequest request) {
        AuthenticatedUser user = authTokenService.requireUser(request);
        return new ResponseEntity<>(packageService.createForVendor(packageDTO, user.id()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageDTO> getPackageById(@PathVariable int id) {
        return ResponseEntity.ok(packageService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PackageDTO>> getAllPackages() {
        return ResponseEntity.ok(packageService.getAll());
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<PackageDTO>> getVendorPackages(
            @PathVariable int vendorId,
            HttpServletRequest request) {
        AuthenticatedUser user = authTokenService.requireUser(request);
        if (user.id() != vendorId) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You can only view your own packages");
        }
        return ResponseEntity.ok(packageService.getPackagesByVendor(vendorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackageDTO> updatePackage(
            @PathVariable int id,
            @RequestBody PackageDTO packageDTO,
            HttpServletRequest request) {
        AuthenticatedUser user = authTokenService.requireUser(request);
        return ResponseEntity.ok(packageService.updateForVendor(id, packageDTO, user.id()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable int id, HttpServletRequest request) {
        AuthenticatedUser user = authTokenService.requireUser(request);
        packageService.deleteForVendor(id, user.id());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<PackageDTO>> getAllAvailablePackages() {
        return ResponseEntity.ok(packageService.getAllAvailable());
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Void> updateAvailability(
            @PathVariable int id,
            @RequestParam String status,
            HttpServletRequest request) {
        AuthenticatedUser user = authTokenService.requireUser(request);
        packageService.updateAvailabilityForVendor(id, status, user.id());
        return ResponseEntity.ok().build();
    }
}
