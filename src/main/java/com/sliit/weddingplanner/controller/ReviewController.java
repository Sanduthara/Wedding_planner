package com.sliit.weddingplanner.controller;

import com.sliit.weddingplanner.dto.ReviewDTO;
import com.sliit.weddingplanner.security.AuthTokenService;
import com.sliit.weddingplanner.security.AuthenticatedUser;
import com.sliit.weddingplanner.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: ReviewController depends on ReviewService
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final AuthTokenService authTokenService;

    @Autowired
    public ReviewController(ReviewService reviewService, AuthTokenService authTokenService) {
        this.reviewService = reviewService;
        this.authTokenService = authTokenService;
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

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByVendor(
            @PathVariable int vendorId,
            HttpServletRequest request) {
        AuthenticatedUser user = authTokenService.requireUser(request);
        if (user.id() != vendorId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only view your own reviews");
        }
        return ResponseEntity.ok(reviewService.getReviewsByVendor(vendorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable int id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
