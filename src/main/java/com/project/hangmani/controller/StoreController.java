package com.project.hangmani.controller;

import com.project.hangmani.dto.ResponseDTO;
import com.project.hangmani.dto.StoreDTO.*;
import com.project.hangmani.service.StoreService;
import com.project.hangmani.util.ConvertData;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
@Slf4j
public class StoreController {
    private final StoreService storeService;
    private final ConvertData convertData;
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
        this.convertData = new ConvertData();
    }
    @GetMapping
    @ResponseBody
    public ResponseDTO<List<ResponseStoreDTO>> getStoresInfo(@ModelAttribute @Valid RequestStoresDTO requestStoreDTO){
        List<ResponseStoreDTO> storeInfo = storeService.getStoreInfo(requestStoreDTO);

        return ResponseDTO.<List<ResponseStoreDTO>>builder()
                .data(storeInfo)
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }

    @GetMapping("/{storeUuid}")
    @ResponseBody
    public ResponseDTO<ResponseStoreDTO> getStoreInfoByUuid(@PathVariable("storeUuid") String storeUuid) {
        return ResponseDTO.<ResponseStoreDTO>builder()
                .data(storeService.getStoreInfo(storeUuid))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();

    }
    @PutMapping("/{storeUuid}")
    @ResponseBody
    public ResponseDTO<ResponseStoreDTO> updateStore(@PathVariable("storeUuid") String storeUuid,
                                                           @RequestBody @Valid RequestStoreUpdateDTO requestStoreUpdateDTO) {
        return ResponseDTO.<ResponseStoreDTO>builder()
                .data(storeService.updateStoreInfo(storeUuid, requestStoreUpdateDTO))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }

    @PostMapping
    @ResponseBody
    public ResponseDTO<ResponseStoreDTO> insertStore(@ModelAttribute @Valid RequestStoreInsertDTO requestStoreDTO) {
        return ResponseDTO.<ResponseStoreDTO>builder()
                .data(storeService.insertStoreInfo(requestStoreDTO))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }

//    @PutMapping("/{storeUuid}")
//    @ResponseBody
//    public ResponseDTO<ResponseStoreChangeDTO> updateStore(@PathVariable("storeUuid") String storeUuid,
//                                    @RequestBody @Valid RequestStoreChangeDTO requestStoreChangeDTO) {
//        return ResponseDTO.<ResponseStoreChangeDTO>builder()
//                .data(storeService.updateStoreInfo(requestStoreChangeDTO))
//                .status(HttpStatus.OK.value())
//                .message(HttpStatus.OK.name())
//                .build();
//    }


}
