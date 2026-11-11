package com.sliit.weddingplanner.controller;

import com.sliit.weddingplanner.dto.BookingDTO;
import com.sliit.weddingplanner.dto.BookingPackageDTO;
import com.sliit.weddingplanner.security.AuthTokenService;
import com.sliit.weddingplanner.security.AuthenticatedUser;
import com.sliit.weddingplanner.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: BookingController depends on BookingService
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final AuthTokenService authTokenService;

    @Autowired
    public BookingController(BookingService bookingService, AuthTokenService authTokenService) {
        this.bookingService = bookingService;
        this.authTokenService = authTokenService;
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO) {
        return new ResponseEntity<>(bookingService.create(bookingDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable int id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByCustomer(@PathVariable int customerId) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomer(customerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateBookingStatus(@PathVariable int id, @RequestParam String status) {
        bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm-event/{eventId}")
    public ResponseEntity<BookingDTO> confirmEvent(
            @PathVariable int eventId, 
            @RequestParam String location,
            @RequestParam(defaultValue = "FULL") String paymentType) {
        return ResponseEntity.ok(bookingService.confirmEvent(eventId, location, paymentType));
    }

    @GetMapping("/{bookingId}/packages")
    public ResponseEntity<List<BookingPackageDTO>> getBookingPackages(@PathVariable int bookingId) {
        return ResponseEntity.ok(bookingService.getBookingPackages(bookingId));
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<BookingPackageDTO>> getVendorBookings(
            @PathVariable int vendorId,
            HttpServletRequest request) {
        AuthenticatedUser user = authTokenService.requireUser(request);
        if (user.id() != vendorId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only view your own booking requests");
        }
        return ResponseEntity.ok(bookingService.getVendorBookings(vendorId));
    }

    @PutMapping("/package/{id}/status")
    public ResponseEntity<Void> updatePackageStatus(
            @PathVariable int id,
            @RequestParam String status,
            @RequestParam(required = false) String reason,
            HttpServletRequest request) {
        AuthenticatedUser user = authTokenService.requireUser(request);
        bookingService.updateBookingPackageStatus(id, status, reason, user.id());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable int id, @RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.update(id, bookingDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable int id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
