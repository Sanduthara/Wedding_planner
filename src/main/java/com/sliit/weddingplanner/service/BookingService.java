package com.sliit.weddingplanner.service;

import com.sliit.weddingplanner.dto.BookingDTO;
import java.util.List;

public interface BookingService {
    List<BookingDTO> getBookingsByCustomer(int customerId);
    void updateBookingStatus(int bookingId, String status);
}
