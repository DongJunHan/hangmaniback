package com.project.hangmani.service;

import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.StoreDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoreDTO;
import com.project.hangmani.dto.StoreDTO.ResponseStoreDTO;
import com.project.hangmani.enums.ResponseStatus;
import com.project.hangmani.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@Transactional
@Service
public class StoreService {
    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public ResponseStoreDTO getStoreInfo(RequestStoreDTO requestStoreDTO){
        List<Store> storeList = storeRepository.findStoreInfoByArea(
                requestStoreDTO.getSido(), requestStoreDTO.getSigugun());
        return new ResponseStoreDTO(storeList, ResponseStatus.OK.getCode(), "success");
    }
}
