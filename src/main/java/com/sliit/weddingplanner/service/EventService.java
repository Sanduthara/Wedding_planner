package com.sliit.weddingplanner.service;

import com.sliit.weddingplanner.dto.EventDTO;

import java.util.List;

// OOP: Interface-based Design
// OOP: Abstraction
public interface EventService {
    EventDTO create(EventDTO dto);
    EventDTO getById(int id);
    List<EventDTO> getAll();
    EventDTO update(int id, EventDTO dto);
    void delete(int id);
    
    List<EventDTO> getEventsByCustomer(int customerId);
    List<EventDTO> getPendingReviewsByCustomer(int customerId);
    void submitEventReview(int eventId, int rating, String review);
}
