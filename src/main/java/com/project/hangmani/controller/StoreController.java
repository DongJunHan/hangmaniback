package com.project.hangmani.controller;

import com.project.hangmani.dto.ResponseDto;
import com.project.hangmani.dto.StoreDTO.RequestStoresDTO;
import com.project.hangmani.dto.StoreDTO.ResponseStoreDTO;
import com.project.hangmani.service.StoreService;
import com.project.hangmani.util.ConvertData;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @GetMapping
    @ResponseBody
    public ResponseDto<List<ResponseStoreDTO>> getStoresInfo(@ModelAttribute @Valid RequestStoresDTO requestStoreDTO){
        List<ResponseStoreDTO> storeInfo = storeService.getStoreInfo(requestStoreDTO);

        return ResponseDto.<List<ResponseStoreDTO>>builder()
                .data(storeInfo)
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }

    @GetMapping("/{storeUuid}")
    @ResponseBody
    public ResponseDto<ResponseStoreDTO> getStoreInfoByUuid(@PathVariable("storeUuid") String storeUuid) {
        return ResponseDto.<ResponseStoreDTO>builder()
                .data(storeService.getStoreInfo(storeUuid))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();

    }
}
