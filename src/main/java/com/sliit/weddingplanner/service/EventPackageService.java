package com.sliit.weddingplanner.service;

import com.sliit.weddingplanner.dto.EventPackageDTO;
import java.util.List;

public interface EventPackageService {
    EventPackageDTO addPackageToEvent(EventPackageDTO dto);
    List<EventPackageDTO> getPackagesByEvent(int eventId);
    void removePackageFromEvent(int eventPackageId);
}
