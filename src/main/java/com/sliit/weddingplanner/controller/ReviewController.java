package com.sliit.weddingplanner.controller;

import com.sliit.weddingplanner.dto.ReviewDTO;
import com.sliit.weddingplanner.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: ReviewController depends on ReviewService
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        return new ResponseEntity<>(reviewService.create(reviewDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable int id) {
        return ResponseEntity.ok(reviewService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAll());
    }

    @GetMapping("/package/{packageId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByPackage(@PathVariable int packageId) {
        return ResponseEntity.ok(reviewService.getReviewsByPackage(packageId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable int id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
