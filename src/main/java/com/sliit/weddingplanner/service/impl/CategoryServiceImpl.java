package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.dto.CategoryDTO;
import com.sliit.weddingplanner.exception.ResourceNotFoundException;
import com.sliit.weddingplanner.repository.CategoryRepository;
import com.sliit.weddingplanner.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// OOP: Encapsulation
// OOP: Inheritance (Implements CategoryService)
// OOP: Polymorphism
// Relationship: CategoryServiceImpl implements CategoryService
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDTO create(CategoryDTO dto) {
        return categoryRepository.save(dto);
    }

    @Override
    public CategoryDTO getById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
    }

    @Override
    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryDTO update(int id, CategoryDTO dto) {
        CategoryDTO existing = getById(id);
        existing.setCategoryName(dto.getCategoryName());
        existing.setDescription(dto.getDescription());
        return categoryRepository.update(existing);
    }

    @Override
    public void delete(int id) {
        getById(id); // Check existence
        categoryRepository.delete(id);
    }
}
