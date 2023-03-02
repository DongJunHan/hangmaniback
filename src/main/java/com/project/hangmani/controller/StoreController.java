package com.project.hangmani.controller;

import com.project.hangmani.dto.DataResponseDTO;
import com.project.hangmani.domain.Store;
import com.project.hangmani.service.StoreService;
import com.project.hangmani.util.ConvertData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
@Slf4j
public class StoreController {
    private final StoreService storeService;
    private final ConvertData convertData;
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
        this.convertData = new ConvertData();
    }
    @GetMapping("/storeinfo-sido")
    @ResponseBody
    public DataResponseDTO<Object> helloString(@RequestParam("sido") String sido, @RequestParam("sigugun") String sigugun){
        List<Store> storeInfo = storeService.getStoreInfo(sido, sigugun);
        log.info("result={}", DataResponseDTO.of(storeInfo).getData());
        return DataResponseDTO.of(storeInfo);
    }
}
