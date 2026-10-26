package com.sliit.weddingplanner.controller.admin;

import com.sliit.weddingplanner.dto.AdminDTO;
import com.sliit.weddingplanner.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: AdminController depends on AdminService
@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<AdminDTO> createAdmin(@RequestBody AdminDTO adminDTO) {
        AdminDTO created = adminService.createUser(adminDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable int id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<AdminDTO>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDTO> updateAdmin(@PathVariable int id, @RequestBody AdminDTO adminDTO) {
        return ResponseEntity.ok(adminService.updateUser(id, adminDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable int id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
