package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.dto.BookingDTO;
import com.sliit.weddingplanner.repository.BookingPackageRepository;
import com.sliit.weddingplanner.repository.BookingRepository;
import com.sliit.weddingplanner.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingPackageRepository bookingPackageRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              BookingPackageRepository bookingPackageRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingPackageRepository = bookingPackageRepository;
    }

    @Override
    public List<BookingDTO> getBookingsByCustomer(int customerId) {
        List<BookingDTO> bookings = bookingRepository.findAllByCustomerId(customerId);
        for (BookingDTO booking : bookings) {
            booking.setPackages(bookingPackageRepository.findAllByBookingId(booking.getBookingId()));
        }
        return bookings;
    }

    @Override
    public void updateBookingStatus(int bookingId, String status) {
        bookingRepository.updateStatus(bookingId, status);
    }
}
