package com.project.hangmani.service;

import com.project.hangmani.convert.ResponseConvert;
import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.StoreDTO.RequestStoreInsertDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoreUpdateDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoresDTO;
import com.project.hangmani.dto.StoreDTO.ResponseStoreDTO;
import com.project.hangmani.exception.AlreadyExistStore;
import com.project.hangmani.exception.FailInsertData;
import com.project.hangmani.exception.FailUpdateStore;
import com.project.hangmani.exception.NotFoundStore;
import com.project.hangmani.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public ResponseStoreDTO updateStoreInfo(String storeUuid, RequestStoreUpdateDTO requestStoreUpdateDTO) {
        //check store exist
        Store storeInfoByUuid = storeRepository.findStoreInfoByUuid(storeUuid);
        if (storeInfoByUuid == null){
            throw new NotFoundStore();
        }
        //update store info
        int ret = storeRepository.updateStoreInfo(storeUuid, requestStoreUpdateDTO);
        if (ret == 0) {
            throw new FailUpdateStore();
        }

        return responseConvert.convertResponseDTO(
                storeRepository.findStoreInfoByUuid(storeUuid));
    }
    @Transactional
    public ResponseStoreDTO insertStoreInfo(RequestStoreInsertDTO requestStoreDTO) {
        //check already exist
        Store findStore = storeRepository.findStoreByNameLatiLongi(requestStoreDTO);
        log.info("store={}", findStore);
        if (findStore != null) {
            throw new AlreadyExistStore();
        }

        int index = storeRepository.insertStoreInfo(requestStoreDTO);
        if (index == 0) {
            throw new FailInsertData();
        }

        //get store info
        return responseConvert.convertResponseDTO(storeRepository.findStoreByNameLatiLongi(requestStoreDTO));
    }
}
