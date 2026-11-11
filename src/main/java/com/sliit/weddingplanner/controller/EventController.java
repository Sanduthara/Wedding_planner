package com.sliit.weddingplanner.controller;

import com.sliit.weddingplanner.dto.EventDTO;
import com.sliit.weddingplanner.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: EventController depends on EventService
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        return new ResponseEntity<>(eventService.create(eventDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable int id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable int id, @RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(eventService.update(id, eventDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable int id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<EventDTO>> getEventsByCustomer(@PathVariable int customerId) {
        return ResponseEntity.ok(eventService.getEventsByCustomer(customerId));
    }

    @GetMapping("/customer/{customerId}/pending-reviews")
    public ResponseEntity<List<EventDTO>> getPendingReviews(@PathVariable int customerId) {
        return ResponseEntity.ok(eventService.getPendingReviewsByCustomer(customerId));
    }

    @PostMapping("/{eventId}/review")
    public ResponseEntity<Void> submitReview(
            @PathVariable int eventId, 
            @RequestParam int rating, 
            @RequestParam String review) {
        eventService.submitEventReview(eventId, rating, review);
        return ResponseEntity.ok().build();
    }
}
