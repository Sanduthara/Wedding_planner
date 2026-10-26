package com.sliit.weddingplanner.controller;

import com.sliit.weddingplanner.dto.VendorDTO;
import com.sliit.weddingplanner.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: VendorController depends on VendorService
@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;

    @Autowired
    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    public ResponseEntity<VendorDTO> registerVendor(@RequestBody VendorDTO vendorDTO) {
        VendorDTO created = vendorService.createUser(vendorDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorDTO> getVendorById(@PathVariable int id) {
        return ResponseEntity.ok(vendorService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<VendorDTO>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorDTO> updateVendor(@PathVariable int id, @RequestBody VendorDTO vendorDTO) {
        return ResponseEntity.ok(vendorService.updateUser(id, vendorDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable int id) {
        vendorService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveVendor(@PathVariable int id, @RequestParam int adminId) {
        vendorService.approveVendor(id, adminId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectVendor(@PathVariable int id, @RequestParam int adminId) {
        vendorService.rejectVendor(id, adminId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Void> updateAvailability(
            @PathVariable int id, 
            @RequestParam String availability,
            @RequestParam(required = false) Integer adminId) {
        vendorService.updateAvailability(id, availability, adminId);
        return ResponseEntity.ok().build();
    }
}
