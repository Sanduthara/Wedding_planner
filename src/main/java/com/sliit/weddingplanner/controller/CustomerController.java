package com.sliit.weddingplanner.controller;

import com.sliit.weddingplanner.dto.CustomerDTO;
import com.sliit.weddingplanner.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: CustomerController depends on CustomerService
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> registerCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO created = customerService.createUser(customerDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable int id) {
        return ResponseEntity.ok(customerService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable int id, @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.updateUser(id, customerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id) {
        customerService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
