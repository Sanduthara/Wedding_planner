package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.dto.*;
import com.sliit.weddingplanner.exception.ResourceNotFoundException;
import com.sliit.weddingplanner.repository.*;
import com.sliit.weddingplanner.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// OOP: Encapsulation
// OOP: Inheritance (Implements BookingService)
// OOP: Polymorphism
// Relationship: BookingServiceImpl implements BookingService
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final EventPackageRepository eventPackageRepository;
    private final BookingPackageRepository bookingPackageRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, 
                              EventRepository eventRepository,
                              EventPackageRepository eventPackageRepository,
                              BookingPackageRepository bookingPackageRepository,
                              PaymentRepository paymentRepository) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
        this.eventPackageRepository = eventPackageRepository;
        this.bookingPackageRepository = bookingPackageRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public BookingDTO create(BookingDTO dto) {
        return bookingRepository.save(dto);
    }

    @Override
    public BookingDTO getById(int id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + id));
    }

    @Override
    public List<BookingDTO> getAll() {
        List<BookingDTO> bookings = bookingRepository.findAll();
        for (BookingDTO b : bookings) {
            b.setPackages(bookingPackageRepository.findAllByBookingId(b.getBookingId()));
        }
        return bookings;
    }

    @Override
    public BookingDTO update(int id, BookingDTO dto) {
        BookingDTO existing = getById(id);
        existing.setEventId(dto.getEventId());
        existing.setCustomerId(dto.getCustomerId());
        existing.setBookingDate(dto.getBookingDate());
        existing.setLocation(dto.getLocation());
        existing.setStatus(dto.getStatus());
        return bookingRepository.update(existing);
    }

    @Override
    public void delete(int id) {
        getById(id); // Throws if not found
        bookingRepository.delete(id);
    }

    @Override
    public List<BookingDTO> getBookingsByCustomer(int customerId) {
        List<BookingDTO> bookings = bookingRepository.findAllByCustomerId(customerId);
        for (BookingDTO b : bookings) {
            List<BookingPackageDTO> pkgs = bookingPackageRepository.findAllByBookingId(b.getBookingId());
            System.out.println("DEBUG: Found " + pkgs.size() + " packages for Booking ID: " + b.getBookingId());
            b.setPackages(pkgs);
        }
        return bookings;
    }

    @Override
    public void updateBookingStatus(int bookingId, String status) {
        bookingRepository.updateStatus(bookingId, status);
    }

    @Override
    @Transactional
    public BookingDTO confirmEvent(int eventId, String location, String paymentType) {
        // 1. Get Event
        EventDTO event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        // 2. Update Event Status and Location
        event.setStatus("PENDING_APPROVAL");
        event.setLocation(location);
        eventRepository.update(event);

        // 3. Create or Get Booking
        BookingDTO booking = bookingRepository.findByEventId(eventId).orElse(null);
        if (booking == null) {
            booking = new BookingDTO();
            booking.setEventId(eventId);
            booking.setCustomerId(event.getCustomerId());
            booking.setBookingDate(event.getEventDate());
            booking.setLocation(location);
            booking.setStatus("PENDING");
            booking.setPaymentType(paymentType);
            booking.setCreatedAt(LocalDateTime.now());
            booking = bookingRepository.save(booking);
        } else {
            booking.setLocation(location);
            booking.setStatus("PENDING");
            booking.setPaymentType(paymentType);
            bookingRepository.update(booking);
            // Clear existing packages for re-selection sync
            bookingPackageRepository.deleteAllByBookingId(booking.getBookingId());
        }

        // 4. Create Booking Packages and calculate total cost
        BigDecimal totalCost = BigDecimal.ZERO;
        List<EventPackageDTO> eventPackages = eventPackageRepository.findAllByEventId(eventId);
        for (EventPackageDTO ep : eventPackages) {
            BigDecimal pkgPrice = ep.getPackagePrice() != null ? ep.getPackagePrice() : BigDecimal.ZERO;
            int qty = ep.getQuantity() != null ? ep.getQuantity() : 1;
            totalCost = totalCost.add(pkgPrice.multiply(BigDecimal.valueOf(qty)));

            BookingPackageDTO bp = new BookingPackageDTO();
            bp.setBookingId(booking.getBookingId());
            bp.setEventPackageId(ep.getEventPackageId());
            bp.setQuantity(qty);
            bp.setPriceAtBooking(pkgPrice);
            bp.setVendorStatus("PENDING");
            bp.setNotes(ep.getNotes());
            bp.setCreatedAt(LocalDateTime.now());
            bookingPackageRepository.save(bp);
        }

        // Update booking with the calculated total cost
        booking.setTotalCost(totalCost.doubleValue());
        bookingRepository.update(booking);

        System.out.println("DEBUG: Booking #" + booking.getBookingId() + " updated with total " + totalCost);

        return booking;
    }

    @Override
    public List<BookingPackageDTO> getBookingPackages(int bookingId) {
        return bookingPackageRepository.findAllByBookingId(bookingId);
    }

    @Override
    public List<BookingPackageDTO> getVendorBookings(int vendorId) {
        return bookingPackageRepository.findAllByVendorId(vendorId);
    }

    @Override
    @Transactional
    public void updateBookingPackageStatus(int id, String status, String reason) {
        System.out.println("DEBUG: Updating status for package ID: " + id + " to " + status);
        
        // 1. Update the individual package status
        bookingPackageRepository.updateVendorStatus(id, status, reason);
        
        // 2. Get the booking ID for this package
        int bookingId = bookingPackageRepository.getBookingIdByPackageId(id);
        System.out.println("DEBUG: Found booking ID: " + bookingId);
        if (bookingId <= 0) return;

        // 3. Check if all packages for this booking are now approved
        List<BookingPackageDTO> allPackages = bookingPackageRepository.findAllByBookingId(bookingId);
        System.out.println("DEBUG: Total packages found for this booking: " + allPackages.size());
        
        boolean allApproved = !allPackages.isEmpty() && allPackages.stream()
                .allMatch(p -> {
                    String s = p.getVendorStatus();
                    System.out.println("DEBUG: Package " + p.getBookingPackageId() + " status: " + s);
                    return s != null && (s.equalsIgnoreCase("APPROVED") || s.equalsIgnoreCase("CONFIRMED"));
                });
        
        System.out.println("DEBUG: All packages approved? " + allApproved);

        // 4. If all approved, transition the booking and event to CONFIRMED
        if (allApproved) {
            System.out.println("DEBUG: All conditions met. Flipping Booking #" + bookingId + " to CONFIRMED");
            
            // Direct update to booking status
            bookingRepository.updateStatus(bookingId, "CONFIRMED");
            System.out.println("DEBUG: Booking status updated in DB");

            // Also update the associated event status
            bookingRepository.findById(bookingId).ifPresent(booking -> {
                eventRepository.findById(booking.getEventId()).ifPresent(event -> {
                    event.setStatus("CONFIRMED");
                    eventRepository.update(event);
                    System.out.println("DEBUG: Event #" + event.getEventId() + " status updated to CONFIRMED");
                });

                // Automated Payment Transition
                paymentRepository.findByBookingId(bookingId).ifPresentOrElse(
                    p -> {
                        p.setStatus("PENDING");
                        p.setTotalAmount(BigDecimal.valueOf(booking.getTotalCost()));
                        p.setDueAmount(BigDecimal.valueOf(booking.getTotalCost()).subtract(p.getAmount()));
                        p.setPaymentType(booking.getPaymentType());
                        paymentRepository.updateStatus(p.getPaymentId(), "PENDING");
                        System.out.println("DEBUG: Payment #" + p.getPaymentId() + " updated to PENDING with type " + booking.getPaymentType());
                    },
                    () -> {
                        PaymentDTO payment = new PaymentDTO();
                        payment.setBookingId(bookingId);
                        payment.setTotalAmount(BigDecimal.valueOf(booking.getTotalCost()));
                        payment.setAmount(BigDecimal.ZERO);
                        payment.setDueAmount(BigDecimal.valueOf(booking.getTotalCost()));
                        payment.setPaymentType(booking.getPaymentType() != null ? booking.getPaymentType() : "FULL");
                        payment.setStatus("PENDING");
                        paymentRepository.save(payment);
                        System.out.println("DEBUG: Automated payment record created as PENDING with type " + payment.getPaymentType());
                    }
                );
            });
        }
    }
}
