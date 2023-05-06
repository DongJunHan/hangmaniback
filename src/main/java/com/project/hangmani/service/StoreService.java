package com.project.hangmani.service;

import com.project.hangmani.convert.RequestConvert;
import com.project.hangmani.convert.ResponseConvert;
import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.FileDTO.RequestStoreFileDTO;
import com.project.hangmani.dto.FileDTO.ResponseStoreFileDTO;
import com.project.hangmani.dto.StoreDTO.*;
import com.project.hangmani.exception.AlreadyExistStore;
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
        Store store = storeRepository.getStoreInfoByUuid(storeUuid);
        if(store.getStoreUuid() == null)
            throw new NotFoundStore();
        log.info("Before store win {},{}, {}, {}", store.getWin1stCount(), store.getWin2stCount(), store.getStoreLatitude(), store.getStoreLongitude());
        if (store.getSavedFileNames().indexOf(storeUuid) == -1) {
            log.info("there is no map marker");
            ResponseStoreFileDTO urls = fileService.getMapAttachmentUrls(
                    RequestStoreFileDTO.builder()
                            .savedFileNames(null)
                            .storeUuid(storeUuid)
                            .storeLatitude(store.getStoreLatitude())
                            .storeLongitude(store.getStoreLongitude())
                            .build());
            for (String url :
                    urls.getDomainUrls()) {
                store.setSavedFileNames(store.getSavedFileNames() + "," + url);
            }
        }
        log.info("After store win {},{}", store.getWin1stCount(), store.getWin2stCount());
        return responseConvert.convertResponseDTO(store);
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
    public List<ResponseStoreFilterDTO> getStoreInfo(RequestStoreFilterDTO requestDTO) {
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

        return responseConvert.convertResponseDTO(storeInfos);
    }

}
