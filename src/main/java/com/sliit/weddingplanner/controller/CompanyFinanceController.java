package com.sliit.weddingplanner.controller;

import com.sliit.weddingplanner.dto.CompanyFinanceDTO;
import com.sliit.weddingplanner.service.CompanyFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: CompanyFinanceController depends on CompanyFinanceService
@RestController
@RequestMapping("/api/finance")
public class CompanyFinanceController {

    private final CompanyFinanceService financeService;

    @Autowired
    public CompanyFinanceController(CompanyFinanceService financeService) {
        this.financeService = financeService;
    }

    @PostMapping
    public ResponseEntity<CompanyFinanceDTO> recordTransaction(@RequestBody CompanyFinanceDTO financeDTO) {
        return new ResponseEntity<>(financeService.create(financeDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyFinanceDTO> getTransactionById(@PathVariable int id) {
        return ResponseEntity.ok(financeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CompanyFinanceDTO>> getAllTransactions() {
        return ResponseEntity.ok(financeService.getAll());
    }
}
