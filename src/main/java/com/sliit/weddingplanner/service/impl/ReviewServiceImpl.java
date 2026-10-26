package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.dto.ReviewDTO;
import com.sliit.weddingplanner.exception.ResourceNotFoundException;
import com.sliit.weddingplanner.repository.ReviewRepository;
import com.sliit.weddingplanner.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// OOP: Encapsulation
// OOP: Inheritance (Implements ReviewService)
// OOP: Polymorphism
// Relationship: ReviewServiceImpl implements ReviewService
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public ReviewDTO create(ReviewDTO dto) {
        return reviewRepository.save(dto);
    }

    @Override
    public ReviewDTO getById(int id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + id));
    }

    @Override
    public List<ReviewDTO> getAll() {
        return reviewRepository.findAll();
    }

    @Override
    public ReviewDTO update(int id, ReviewDTO dto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void delete(int id) {
        reviewRepository.delete(id);
    }

    @Override
    public List<ReviewDTO> getReviewsByVendor(int vendorId) {
        return reviewRepository.findAllByVendorId(vendorId);
    }

    @Override
    public List<ReviewDTO> getReviewsByPackage(int packageId) {
        return reviewRepository.findAllByPackageId(packageId);
    }
}
