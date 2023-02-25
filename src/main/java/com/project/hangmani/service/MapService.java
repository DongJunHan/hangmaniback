package com.project.hangmani.service;

import com.project.hangmani.model.store.MapRepository;
import org.springframework.stereotype.Service;

@Service
public class MapService {
    private final MapRepository mapRepository;

    public MapService(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }
}
