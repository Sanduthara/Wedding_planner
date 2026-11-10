package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.dto.EventPackageDTO;
import com.sliit.weddingplanner.repository.EventPackageRepository;
import com.sliit.weddingplanner.service.EventPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventPackageServiceImpl implements EventPackageService {

    private final EventPackageRepository eventPackageRepository;

    @Autowired
    public EventPackageServiceImpl(EventPackageRepository eventPackageRepository) {
        this.eventPackageRepository = eventPackageRepository;
    }

    @Override
    public EventPackageDTO addPackageToEvent(EventPackageDTO dto) {
        return eventPackageRepository.save(dto);
    }

    @Override
    public List<EventPackageDTO> getPackagesByEvent(int eventId) {
        return eventPackageRepository.findAllByEventId(eventId);
    }

    @Override
    public void removePackageFromEvent(int eventPackageId) {
        eventPackageRepository.delete(eventPackageId);
    }
}
