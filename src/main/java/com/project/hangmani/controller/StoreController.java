package com.project.hangmani.controller;

import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.StoreDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoreDTO;
import com.project.hangmani.dto.StoreDTO.ResponseStoreDTO;
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
    //(@RequestParam("sido") String sido, @RequestParam("sigugun") String sigugun){
    public ResponseStoreDTO helloString(RequestStoreDTO requestStoreDTO){
        ResponseStoreDTO storeDTO = storeService.getStoreInfo(requestStoreDTO);

        return new ResponseStoreDTO();
    }
}
