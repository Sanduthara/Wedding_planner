package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.dto.CompanyFinanceDTO;
import com.sliit.weddingplanner.exception.ResourceNotFoundException;
import com.sliit.weddingplanner.repository.CompanyFinanceRepository;
import com.sliit.weddingplanner.service.CompanyFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// OOP: Encapsulation
// OOP: Inheritance (Implements CompanyFinanceService)
// OOP: Polymorphism
// Relationship: CompanyFinanceServiceImpl implements CompanyFinanceService
@Service
public class CompanyFinanceServiceImpl implements CompanyFinanceService {

    private final CompanyFinanceRepository financeRepository;

    @Autowired
    public CompanyFinanceServiceImpl(CompanyFinanceRepository financeRepository) {
        this.financeRepository = financeRepository;
    }

    @Override
    public CompanyFinanceDTO create(CompanyFinanceDTO dto) {
        return financeRepository.save(dto);
    }

    @Override
    public CompanyFinanceDTO getById(int id) {
        return financeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Finance record not found with id " + id));
    }

    @Override
    public List<CompanyFinanceDTO> getAll() {
        return financeRepository.findAll();
    }

    @Override
    public CompanyFinanceDTO update(int id, CompanyFinanceDTO dto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
