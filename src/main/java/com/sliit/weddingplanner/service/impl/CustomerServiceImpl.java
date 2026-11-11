package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.dto.CustomerDTO;
import com.sliit.weddingplanner.exception.DuplicateRecordException;
import com.sliit.weddingplanner.repository.CustomerRepository;
import com.sliit.weddingplanner.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return customerRepository.save(customerDTO);
    }
}
