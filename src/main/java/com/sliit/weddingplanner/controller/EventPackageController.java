package com.sliit.weddingplanner.controller;

import com.sliit.weddingplanner.dto.EventPackageDTO;
import com.sliit.weddingplanner.service.EventPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-packages")
public class EventPackageController {

    private final EventPackageService eventPackageService;

    @Autowired
    public EventPackageController(EventPackageService eventPackageService) {
        this.eventPackageService = eventPackageService;
    }

    @PostMapping
    public ResponseEntity<EventPackageDTO> addPackageToEvent(@RequestBody EventPackageDTO dto) {
        return new ResponseEntity<>(eventPackageService.addPackageToEvent(dto), HttpStatus.CREATED);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventPackageDTO>> getPackagesByEvent(@PathVariable int eventId) {
        return ResponseEntity.ok(eventPackageService.getPackagesByEvent(eventId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removePackageFromEvent(@PathVariable int id) {
        eventPackageService.removePackageFromEvent(id);
        return ResponseEntity.noContent().build();
    }
}
