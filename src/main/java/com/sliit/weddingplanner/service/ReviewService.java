package com.sliit.weddingplanner.service;

import com.sliit.weddingplanner.dto.ReviewDTO;

import java.util.List;

// OOP: Interface-based Design
// OOP: Abstraction
public interface ReviewService {
    ReviewDTO create(ReviewDTO dto);
    ReviewDTO getById(int id);
    List<ReviewDTO> getAll();
    ReviewDTO update(int id, ReviewDTO dto);
    void delete(int id);

    List<ReviewDTO> getReviewsByVendor(int vendorId);
    List<ReviewDTO> getReviewsByPackage(int packageId);
}
