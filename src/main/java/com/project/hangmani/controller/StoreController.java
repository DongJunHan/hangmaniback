package com.project.hangmani.controller;

import com.project.hangmani.model.store.StoreRepository;
import com.project.hangmani.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
@Slf4j
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }
}
