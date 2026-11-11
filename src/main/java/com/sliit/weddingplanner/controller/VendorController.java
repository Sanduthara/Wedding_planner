package com.sliit.weddingplanner.controller;

import com.sliit.weddingplanner.dto.VendorDTO;
import com.sliit.weddingplanner.security.AuthTokenService;
import com.sliit.weddingplanner.security.AuthenticatedUser;
import com.sliit.weddingplanner.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: VendorController depends on VendorService
@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;
    private final AuthTokenService authTokenService;

    @Autowired
    public VendorController(VendorService vendorService, AuthTokenService authTokenService) {
        this.vendorService = vendorService;
        this.authTokenService = authTokenService;
    }

    @PostMapping
    public ResponseEntity<VendorDTO> registerVendor(@RequestBody VendorDTO vendorDTO) {
        VendorDTO created = vendorService.createUser(vendorDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorDTO> getVendorById(@PathVariable int id, HttpServletRequest request) {
        AuthenticatedUser user = authTokenService.requireUser(request);
        if (!"ROLE_ADMIN".equals(user.role()) && user.id() != id) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only view your own vendor profile");
        }
        return ResponseEntity.ok(vendorService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<VendorDTO>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorDTO> updateVendor(
            @PathVariable int id,
            @RequestBody VendorDTO vendorDTO,
            HttpServletRequest request) {
        requireAdmin(request);
        return ResponseEntity.ok(vendorService.updateUser(id, vendorDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable int id, HttpServletRequest request) {
        requireAdmin(request);
        vendorService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveVendor(@PathVariable int id, HttpServletRequest request) {
        AuthenticatedUser admin = requireAdmin(request);
        vendorService.approveVendor(id, admin.id());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectVendor(@PathVariable int id, HttpServletRequest request) {
        AuthenticatedUser admin = requireAdmin(request);
        vendorService.rejectVendor(id, admin.id());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Void> updateAvailability(
            @PathVariable int id, 
            @RequestParam String availability,
            @RequestParam(required = false) Integer adminId,
            HttpServletRequest request) {
        AuthenticatedUser user = authTokenService.requireUser(request);
        Integer actingAdminId = null;
        if ("ROLE_ADMIN".equals(user.role())) {
            actingAdminId = user.id();
            if (!"AVAILABLE".equals(availability) && !"UNAVAILABLE".equals(availability)
                    && !"DISABLED".equals(availability)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid vendor availability");
            }
            if (adminId != null && !adminId.equals(user.id())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin identity does not match the authenticated user");
            }
        } else if (user.id() != id) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only change your own availability");
        } else if (!"AVAILABLE".equals(availability) && !"UNAVAILABLE".equals(availability)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid vendor availability");
        }
        vendorService.updateAvailability(id, availability, actingAdminId);
        return ResponseEntity.ok().build();
    }

    private AuthenticatedUser requireAdmin(HttpServletRequest request) {
        AuthenticatedUser user = authTokenService.requireUser(request);
        if (!"ROLE_ADMIN".equals(user.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Administrator access is required");
        }
        return user;
    }
}
