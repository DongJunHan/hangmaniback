package com.project.hangmani.controller;

import com.project.hangmani.service.MapService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MapController {
    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }
}
