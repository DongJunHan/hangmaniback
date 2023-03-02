package com.project.hangmani.service;

import com.project.hangmani.domain.Store;
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

    public List<Store> getStoreInfo(String sido, String sigugun){
        return storeRepository.findStoreInfoByArea(sido, sigugun);
    }
}
