package com.project.hangmani.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hangmani.model.store.Store;
import com.project.hangmani.model.store.StoreRepository;
import com.project.hangmani.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
@Slf4j
public class StoreController {
    private final StoreService storeService;
    private ObjectMapper objectMapper = new ObjectMapper();
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }
    @GetMapping("/storeinfo-sido")
    @ResponseBody
    public String helloString(@RequestParam("sido") String sido, @RequestParam("sigugun") String sigugun){
        List<Store> storeInfo = storeService.getStoreInfo(sido, sigugun);
        try {
            String result = objectMapper.writeValueAsString(storeInfo);
            return result;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
