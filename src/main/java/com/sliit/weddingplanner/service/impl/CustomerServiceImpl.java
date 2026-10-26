package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.dto.CustomerDTO;
import com.sliit.weddingplanner.exception.DuplicateRecordException;
import com.sliit.weddingplanner.exception.ResourceNotFoundException;
import com.sliit.weddingplanner.repository.CustomerRepository;
import com.sliit.weddingplanner.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// OOP: Encapsulation
// OOP: Inheritance (Implements CustomerService)
// OOP: Polymorphism
// Relationship: CustomerServiceImpl implements CustomerService
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    
    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO createUser(CustomerDTO customerDTO) {
        if (customerRepository.existsByUsername(customerDTO.getUsername())) {
            throw new DuplicateRecordException("Username already in use");
        }
        if (customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new DuplicateRecordException("Email already in use");
        }
        if (customerRepository.existsByPhone(customerDTO.getPhone())) {
            throw new DuplicateRecordException("Phone number already in use");
        }
        // Password encoding removed
        return customerRepository.save(customerDTO);
    }

    @Override
    public CustomerDTO getUserById(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
    }

    @Override
    public List<CustomerDTO> getAllUsers() {
        return customerRepository.findAll();
    }

    @Override
    public CustomerDTO updateUser(int id, CustomerDTO user) {
        CustomerDTO existing = getUserById(id);
        existing.setTitle(user.getTitle());
        existing.setName(user.getName());
        existing.setUsername(user.getUsername());
        existing.setCustomerRole(user.getCustomerRole());
        existing.setOtherPartyName(user.getOtherPartyName());
        existing.setEmail(user.getEmail());
        existing.setPhone(user.getPhone());
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existing.setPassword(user.getPassword());
        }
        return customerRepository.update(existing);
    }

    @Override
    public void deleteUser(int id) {
        getUserById(id); // Throws ResourceNotFoundException if not found
        customerRepository.delete(id);
    }
}
