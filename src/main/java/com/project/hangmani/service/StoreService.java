package com.project.hangmani.service;

import com.project.hangmani.convert.ResponseConvert;
import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.StoreDTO.RequestStoreDTO;
import com.project.hangmani.dto.StoreDTO.ResponseStoreDTO;
import com.project.hangmani.repository.StoreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//@Transactional
@Service
public class StoreService {
    private final StoreRepository storeRepository;

    private final ResponseConvert responseConvert;

    public StoreService(StoreRepository storeRepository) {
        this.responseConvert = new ResponseConvert();
        this.storeRepository = storeRepository;
    }

    public List<ResponseStoreDTO> getStoreInfo(RequestStoreDTO requestStoreDTO){
        return storeRepository.findStoreInfoByArea(requestStoreDTO)
                .stream()
                .map(responseConvert::convertResponseDTO)
                .toList();
    }
}
