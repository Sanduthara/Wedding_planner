package com.sliit.weddingplanner.controller;

import com.sliit.weddingplanner.dto.CustomerDTO;
import com.sliit.weddingplanner.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
