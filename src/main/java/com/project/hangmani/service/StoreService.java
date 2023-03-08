package com.project.hangmani.service;

import com.project.hangmani.convert.ResponseConvert;
import com.project.hangmani.dto.StoreDTO.RequestStoresDTO;
import com.project.hangmani.dto.StoreDTO.ResponseStoreDTO;
import com.project.hangmani.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StoreService {
    private final StoreRepository storeRepository;

    private final ResponseConvert responseConvert;

    public StoreService(StoreRepository storeRepository) {
        this.responseConvert = new ResponseConvert();
        this.storeRepository = storeRepository;
    }

    public List<ResponseStoreDTO> getStoreInfo(RequestStoresDTO requestStoresDTO){
        return storeRepository.findStoreInfoByLatitudeLongitude(requestStoresDTO)
                .stream()
                .map(responseConvert::convertResponseDTO)
                .toList();
    }

    public ResponseStoreDTO getStoreInfo(String storeUuid) {
        return responseConvert.convertResponseDTO(
                storeRepository.findStoreInfoByUuid(
                        storeUuid));
    }
}
