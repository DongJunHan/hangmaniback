package com.project.hangmani.controller;

import com.project.hangmani.common.dto.Response;
import com.project.hangmani.store.model.dto.*;
import com.project.hangmani.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
@CrossOrigin
@Slf4j
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/{storeUuid}")
    @ResponseBody
    public Response<ResponseDTO> getStoreInfoByUuid(@PathVariable("storeUuid") String storeUuid) {
        return Response.<ResponseDTO>builder()
                .data(storeService.getStoreInfo(storeUuid))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();

    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response<List<ResponseFilterDTO>> getStoreInfoByFilter(@RequestBody @Valid RequestFilterDTO requestDTO) {
        log.info("Store Controller filter");
        List<ResponseFilterDTO> storeInfoList = storeService.getStoreInfo(requestDTO);
        return Response.<List<ResponseFilterDTO>>builder()
                .data(storeInfoList)
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }
//    @GetMapping
//    @ResponseBody
//    public Response<List<ResponseFilterDTO>> getStoreInfoByFilter(@ModelAttribute @Valid RequestFilterAreaDTO requestDTO) {
//        log.info("Store Controller Area");
//        List<ResponseFilterDTO> storeInfoList = storeService.getStoreInfo(requestDTO);
//        return Response.<List<ResponseFilterDTO>>builder()
//                .data(storeInfoList)
//                .status(HttpStatus.OK.value())
//                .message(HttpStatus.OK.name())
//                .build();
//    }

    @PutMapping(path = "/{storeUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response<ResponseDTO> updateStore(@PathVariable("storeUuid") String storeUuid,
                                                  @RequestBody @Valid RequestUpdateDTO requestStoreUpdateDTO) {
        return Response.<ResponseDTO>builder()
                .data(storeService.updateStoreInfo(storeUuid, requestStoreUpdateDTO))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response<ResponseDTO> insertStore(@RequestBody @Valid RequestInsertDTO requestStoreDTO) {
        return Response.<ResponseDTO>builder()
                .data(storeService.add(requestStoreDTO))
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.name())
                .build();
    }
}
