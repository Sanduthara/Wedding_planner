package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.dto.EventDTO;
import com.sliit.weddingplanner.exception.ResourceNotFoundException;
import com.sliit.weddingplanner.repository.EventRepository;
import com.sliit.weddingplanner.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// OOP: Encapsulation
// OOP: Inheritance (Implements EventService)
// OOP: Polymorphism
// Relationship: EventServiceImpl implements EventService
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventDTO create(EventDTO dto) {
        return eventRepository.save(dto);
    }

    @Override
    public EventDTO getById(int id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
    }

    @Override
    public List<EventDTO> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public EventDTO update(int id, EventDTO dto) {
        EventDTO existing = getById(id);
        existing.setEventName(dto.getEventName());
        existing.setEventDate(dto.getEventDate());
        existing.setLocation(dto.getLocation());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setEventRating(dto.getEventRating());
        existing.setEventReview(dto.getEventReview());
        return eventRepository.update(existing);
    }

    @Override
    public void delete(int id) {
        getById(id); // Check existence
        eventRepository.delete(id);
    }

    @Override
    public List<EventDTO> getEventsByCustomer(int customerId) {
        return eventRepository.findAllByCustomerId(customerId);
    }

    @Override
    public List<EventDTO> getPendingReviewsByCustomer(int customerId) {
        return eventRepository.findPendingReviewsByCustomerId(customerId);
    }

    @Override
    public void submitEventReview(int eventId, int rating, String review) {
        eventRepository.submitReview(eventId, rating, review);
    }
}
