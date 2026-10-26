package com.sliit.weddingplanner.service;

import com.sliit.weddingplanner.dto.BookingDTO;

import java.util.List;

// OOP: Interface-based Design
// OOP: Abstraction
public interface BookingService {
    BookingDTO create(BookingDTO dto);
    BookingDTO getById(int id);
    List<BookingDTO> getAll();
    BookingDTO update(int id, BookingDTO dto);
    void delete(int id);

    List<BookingDTO> getBookingsByCustomer(int customerId);
    void updateBookingStatus(int bookingId, String status);
    BookingDTO confirmEvent(int eventId, String location, String paymentType);
    List<com.sliit.weddingplanner.dto.BookingPackageDTO> getBookingPackages(int bookingId);
    List<com.sliit.weddingplanner.dto.BookingPackageDTO> getVendorBookings(int vendorId);
    void updateBookingPackageStatus(int id, String status, String reason);
}
