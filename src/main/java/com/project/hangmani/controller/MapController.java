package com.project.hangmani.controller;

import com.project.hangmani.service.MapService;
import com.project.hangmani.util.ConvertData;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MapController {
    private final MapService mapService;
    private final ConvertData convertData;

    public MapController(MapService mapService) {
        this.mapService = mapService;
        this.convertData = new ConvertData();
    }
}
