package com.sliit.weddingplanner.controller;

import com.sliit.weddingplanner.dto.BookingDTO;
import com.sliit.weddingplanner.dto.BookingPackageDTO;
import com.sliit.weddingplanner.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: BookingController depends on BookingService
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
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
    public ResponseEntity<List<BookingPackageDTO>> getVendorBookings(@PathVariable int vendorId) {
        return ResponseEntity.ok(bookingService.getVendorBookings(vendorId));
    }

    @PutMapping("/package/{id}/status")
    public ResponseEntity<Void> updatePackageStatus(@PathVariable int id, @RequestParam String status, @RequestParam(required = false) String reason) {
        bookingService.updateBookingPackageStatus(id, status, reason);
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
