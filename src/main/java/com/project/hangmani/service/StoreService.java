package com.project.hangmani.service;

import com.project.hangmani.convert.RequestConvert;
import com.project.hangmani.convert.ResponseConvert;
import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.StoreDTO;
import com.project.hangmani.dto.StoreDTO.*;
import com.project.hangmani.exception.AlreadyExistStore;
import com.project.hangmani.exception.FailInsertData;
import com.project.hangmani.exception.FailUpdateStore;
import com.project.hangmani.exception.NotFoundStore;
import com.project.hangmani.repository.StoreRepository;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class StoreService {
    private final StoreRepository storeRepository;
    private final FileService fileService;

    private final ResponseConvert responseConvert;
    private final RequestConvert requestConvert;

    public StoreService(StoreRepository storeRepository, FileService fileService) {
        this.responseConvert = new ResponseConvert(new Util());
        this.storeRepository = storeRepository;
        this.requestConvert = new RequestConvert();
        this.fileService = fileService;
    }

    public List<ResponseStoreDTO> getStoreInfo(RequestStoresDTO requestStoresDTO){
        return storeRepository.getStoreInfoByLatitudeLongitude(requestStoresDTO)
                .stream()
                .map(responseConvert::convertResponseDTO)
                .toList();
    }

    public ResponseStoreDTO getStoreInfo(String storeUuid) {
        return responseConvert.convertResponseDTO(
                storeRepository.getStoreInfoByUuid(
                        storeUuid));
    }
    @Transactional
    public ResponseStoreDTO updateStoreInfo(String storeUuid, RequestStoreUpdateDTO requestStoreUpdateDTO) {
        //check store exist
        Store storeInfoByUuid = storeRepository.getStoreInfoByUuid(storeUuid);
        if (storeInfoByUuid == null){
            throw new NotFoundStore();
        }
        //update store info
        int ret = storeRepository.updateStoreInfo(storeUuid, requestStoreUpdateDTO);
        if (ret == 0) {
            throw new FailUpdateStore();
        }

        return responseConvert.convertResponseDTO(
                storeRepository.getStoreInfoByUuid(storeUuid));
    }
    @Transactional
    public ResponseStoreDTO insertStoreInfo(RequestStoreInsertDTO requestStoreDTO) {
        //check already exist
        Store findStore = storeRepository.getStoreByNameLatiLongi(requestStoreDTO);
        log.info("store={}", findStore);
        if (findStore != null) {
            throw new AlreadyExistStore();
        }

        String storeUuid = storeRepository.insertStoreInfo(requestStoreDTO);
        fileService.insertAttachment(requestConvert.convertDTO(requestStoreDTO, storeUuid));



        //get store info
        return responseConvert.convertResponseDTO(storeRepository.getStoreInfoByUuid(storeUuid));
    }

    public ResponseStoreChangeDTO updateStoreInfo(RequestStoreChangeDTO requestStoreChangeDTO) {
        return null;
    }

    /*
    WHERE (34.49350 < s.storelatitude and s.storelatitude < 37.523217) and (126.68852 < s.storelongitude and s.storelongitude < 126.74672)

   SELECT s.storeUuid, s.storeName, l.lottoname,
SUM(CASE WHEN w.winRank = 1 THEN 1 ELSE 0 END) AS win1stCount,
SUM(CASE WHEN w.winRank = 2 THEN 1 ELSE 0 END) AS win2stCount
FROM
    store s
    JOIN win_history w ON s.storeUuid = w.storeUuid
    JOIN lotto_type l ON w.lottoId = l.lottoId
where s.storesido='인천' and s.storesigugun='부평구'
GROUP BY
    s.storeUuid,
    s.storeName,
    w.winRank,
   l.lottoname
order by l.lottoname, win1stcount, win2stcount desc;

     */
    public List<ResponseStoreDTO> getStoreInfo(RequestStoreFilterDTO requestDTO) {
        List<Store> storeInfos;
        if (requestDTO.getStartLatitude() == null
                || requestDTO.getStartLatitude() == null
                || requestDTO.getStartLongitude() == null
                || requestDTO.getEndLongitude() == null) {
            storeInfos = storeRepository.getStoreInfoWithWinCountBySidoSigugun(requestDTO);
        } else {
            storeInfos = storeRepository.getStoreInfoWithWinCountByLatitudeLongitude(requestDTO);
        }
        log.info("storeinfo={}",storeInfos);
        //TODO

        responseConvert.convertResponseDTO(storeInfos);
        return null;
    }
}
