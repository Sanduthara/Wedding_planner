package com.sliit.weddingplanner.service;

import com.sliit.weddingplanner.dto.CompanyFinanceDTO;

import java.util.List;

// OOP: Interface-based Design
// OOP: Abstraction
public interface CompanyFinanceService {
    CompanyFinanceDTO create(CompanyFinanceDTO dto);
    CompanyFinanceDTO getById(int id);
    List<CompanyFinanceDTO> getAll();
    CompanyFinanceDTO update(int id, CompanyFinanceDTO dto);
    void delete(int id);
}
