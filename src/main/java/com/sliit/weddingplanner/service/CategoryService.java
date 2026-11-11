package com.sliit.weddingplanner.service;

import com.sliit.weddingplanner.dto.CategoryDTO;
import java.util.List;

// OOP: Interface-based Design
// OOP: Abstraction
public interface CategoryService {
    CategoryDTO create(CategoryDTO dto);
    CategoryDTO getById(int id);
    List<CategoryDTO> getAll();
    CategoryDTO update(int id, CategoryDTO dto);
    void delete(int id);
}
