package com.project.hangmani.service;

import com.project.hangmani.convert.RequestConvert;
import com.project.hangmani.convert.ResponseConvert;
import com.project.hangmani.domain.LottoType;
import com.project.hangmani.domain.Store;
import com.project.hangmani.domain.StoreAttachment;
import com.project.hangmani.dto.FileDTO.RequestStoreFileDTO;
import com.project.hangmani.dto.FileDTO.ResponseStoreFileDTO;
import com.project.hangmani.dto.StoreDTO.*;
import com.project.hangmani.exception.AlreadyExistStore;
import com.project.hangmani.exception.FailUpdateStore;
import com.project.hangmani.exception.NotFoundStore;
import com.project.hangmani.repository.StoreRepository;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * storeuuid를 가지고 상점정보 1개를 가져오는 함수.
     * 실제로는 쿼리를 세 번 호출해서 정보를 가지고 온다
     * @param storeUuid
     * @return
     */
    public ResponseStoreDTO getStoreInfo(String storeUuid) {
        Store store;
        List<Store> stores = storeRepository.getStoreInfoByUuid(storeUuid);
        stores = processStoreFilterData(stores);
        if (stores.isEmpty())
            throw new NotFoundStore("상점정보를 찾을 수 없습니다.");
        List<LottoType> lottoTypes = storeRepository.getLottoNameByUuid(storeUuid);
        stores = processStoreFilterData(lottoTypes, stores);
        List<StoreAttachment> storeAttachments = storeRepository.getStoreAttachmentByUuid(storeUuid);
        stores = processStoreAttachmentData(storeAttachments, stores);
        if (stores.isEmpty())
            throw new NotFoundStore("상점정보를 찾을 수 없습니다.");
        store = stores.get(0);
        if (store.getSavedFileNames().indexOf(storeUuid) == -1) {
            log.info("there is no map marker");
            ResponseStoreFileDTO urls = fileService.getMapAttachmentUrls(
                    RequestStoreFileDTO.builder()
                            .savedFileNames(null)
                            .storeUuid(storeUuid)
                            .storeLatitude(store.getStoreLatitude())
                            .storeLongitude(store.getStoreLongitude())
                            .build());
            if (null != urls){
                for (String url :
                        urls.getDomainUrls()) {
                    store.setSavedFileNames(store.getSavedFileNames() + "," + url);
                }
            }
        }
        log.info("After store {}", store);
        return responseConvert.convertResponseDTO(store);
    }
    @Transactional
    public ResponseStoreDTO updateStoreInfo(String storeUuid, RequestStoreUpdateDTO requestStoreUpdateDTO) {
        List<Store> stores = null;
        Store storeInfoByUuid = null;
        try {
            //check store exist
            stores = storeRepository.getStoreInfoByUuid(storeUuid);
            storeInfoByUuid = stores.get(0);
            if (storeInfoByUuid == null) {
                throw new NotFoundStore();
            }
            //update store info
            int ret = storeRepository.updateStoreInfo(storeUuid, requestStoreUpdateDTO);
            if (ret == 0) {
                throw new FailUpdateStore();
            }

            stores = storeRepository.getStoreInfoByUuid(storeUuid);
            storeInfoByUuid = stores.get(0);

        }catch (EmptyResultDataAccessException e){
            throw new NotFoundStore("상점정보를 찾을 수 없습니다.", e);
        }
        return responseConvert.convertResponseDTO(storeInfoByUuid);
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
        List<Store> stores = storeRepository.getStoreInfoByUuid(storeUuid);
        findStore = stores.get(0);
        return responseConvert.convertResponseDTO(findStore);
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
    @Transactional
    public List<ResponseStoreFilterDTO> getStoreInfo(RequestStoreFilterDTO requestDTO) {
        requestDTO = checkFilterValid(requestDTO);
        List<Store> storeInfos;
        if (requestDTO.getStartLatitude() == null
                || requestDTO.getEndLatitude() == null
                || requestDTO.getStartLongitude() == null
                || requestDTO.getEndLongitude() == null
                || requestDTO.getStartLatitude() == 0
                || requestDTO.getEndLatitude() == 0
                || requestDTO.getStartLongitude() == 0
                || requestDTO.getEndLongitude() == 0) {
            storeInfos = storeRepository.getStoreInfoWithWinRankBySidoSigugun(requestDTO);
            storeInfos = processStoreFilterData(storeInfos);
            List<LottoType> lottoTypes = storeRepository.getLottoNameBySidoSigugun(requestDTO);
            storeInfos = processStoreFilterData(lottoTypes, storeInfos);
            List<StoreAttachment> storeAttachments = storeRepository.getStoreAttachmentBySidoSigugun(requestDTO);
            storeInfos = processStoreAttachmentData(storeAttachments, storeInfos);
        } else{
            storeInfos = storeRepository.getStoreInfoWithWinRankByLatitudeLongitude(requestDTO);
            storeInfos = processStoreFilterData(storeInfos);
            List<LottoType> lottoTypes = storeRepository.getLottoNameBySidoSigugun(requestDTO);
            storeInfos = processStoreFilterData(lottoTypes, storeInfos);
            List<StoreAttachment> storeAttachments = storeRepository.getStoreAttachmentBySidoSigugun(requestDTO);
            storeInfos = processStoreAttachmentData(storeAttachments, storeInfos);
        }
        //check storeIsActivity
        storeInfos = removeClosedStore(storeInfos);
        //sort
        if (requestDTO.getFilter().equals("2st")) {
            Comparator<Store> second = Comparator.comparingInt(Store::getWin2stCount);
            Collections.sort(storeInfos, second.reversed());
        } else if(requestDTO.getFilter().equals("1st")) {
            Comparator<Store> first = Comparator.comparingInt(Store::getWin1stCount);
            Collections.sort(storeInfos, first.reversed());
        } else{
            Comparator<Store> distance = Comparator.comparingDouble(Store::getDistance);
            Collections.sort(storeInfos, distance);
        }
        // limit
        if (requestDTO.getLimit() > 0) {
            storeInfos = storeInfos.stream().limit(requestDTO.getLimit()).collect(Collectors.toList());
        }
        if (requestDTO.getOffset() > 0){
            for (int i = (storeInfos.size() - requestDTO.getOffset()); i < requestDTO.getLimit(); i++){
                storeInfos.remove(0);
            }
        }
        return responseConvert.convertResponseDTO(storeInfos);
    }

    /**
     * 함수가 정상적으로 실행되려면 uuid를 기준으로 정렬이 필요함
     * @param storeAttachments
     * @param store
     * @return
     */
    private List<Store> processStoreAttachmentData(List<StoreAttachment> storeAttachments, List<Store> store) {
        Map<String, String> storeAttachment = new HashMap<>();
        String storeUuid = null;
        for (int i = 0; i < storeAttachments.size(); i++) {
            if (null == storeUuid) {
                storeUuid = storeAttachments.get(i).getStoreUuid();
                if (null == storeAttachments.get(i).getSavedFileName())
                    storeAttachments.get(i).setSavedFileName("");
                storeAttachment.put(storeUuid, storeAttachments.get(i).getSavedFileName());
            } else if(!storeUuid.equals(storeAttachments.get(i).getStoreUuid())) {
                storeUuid = storeAttachments.get(i).getStoreUuid();
                if (null == storeAttachments.get(i).getSavedFileName())
                    storeAttachments.get(i).setSavedFileName("");
                storeAttachment.put(storeUuid, storeAttachments.get(i).getSavedFileName());
            } else if (storeUuid.equals(storeAttachments.get(i).getStoreUuid())){
                String savedFileNames = storeAttachment.get(storeUuid);
                savedFileNames = savedFileNames + "," + storeAttachments.get(i).getSavedFileName();
                storeAttachment.put(storeUuid, savedFileNames);
            }
        }
        for (int i = 0; i < store.size(); i++) {
            String savedFileNames = storeAttachment.get(store.get(i).getStoreUuid());
            store.get(i).setSavedFileNames(savedFileNames);
        }
        return store;
    }

    /**
     * 함수가 정상적으로 실행되려면 uuid를 기준으로 정렬이 필요함
     * @param lottoTypes
     * @param store
     * @return
     */
    private List<Store> processStoreFilterData(List<LottoType> lottoTypes, List<Store> store) {
        Map<String, String> lottoType = new HashMap<>();
        String storeUuid = null;
        for (int i = 0; i < lottoTypes.size(); i++) {
            if (null == storeUuid) {
                storeUuid = lottoTypes.get(i).getStoreUuid();
                lottoType.put(storeUuid, lottoTypes.get(i).getLottoName());
            } else if(!storeUuid.equals(lottoTypes.get(i).getStoreUuid())) {
                storeUuid = lottoTypes.get(i).getStoreUuid();
                lottoType.put(storeUuid, lottoTypes.get(i).getLottoName());
            } else if (storeUuid.equals(lottoTypes.get(i).getStoreUuid())){
                String lottoName = lottoType.get(storeUuid);
                lottoName =lottoName + "," + lottoTypes.get(i).getLottoName();
                lottoType.put(storeUuid, lottoName);
            }
        }
        for (int i = 0; i < store.size(); i++) {
            String lottoName = lottoType.get(store.get(i).getStoreUuid());
            store.get(i).setLottoName(lottoName);
        }
        return store;
    }

    /**
     * 함수가 정상적으로 실행되려면 uuid를 기준으로 정렬이 필요함
     * @param store
     * @return
     */
    private List<Store> processStoreFilterData(List<Store> store) {
        List<Store> storeList = new ArrayList<>();
        String storeUuid = null;
        int win1Count;
        int win2Count;
        int j = 0;
        for (int i = 0; i < store.size(); i++) {
            storeUuid = store.get(i).getStoreUuid();
            win1Count = 0;
            win2Count = 0;
            storeList.add(Store.builder()
                    .storeUuid(storeUuid)
                    .storeAddress(store.get(i).getStoreAddress())
                    .storeName(store.get(i).getStoreName())
                    .storeSido(store.get(i).getStoreSido())
                    .storeSigugun(store.get(i).getStoreSigugun())
                    .storeLatitude(store.get(i).getStoreLatitude())
                    .storeLongitude(store.get(i).getStoreLongitude())
                    .distance(store.get(i).getDistance())
                    .win1stCount(win1Count)
                    .win2stCount(win2Count)
                    .savedFileNames("")
                    .lottoName("")
                    .build());

            for (j = i; j < store.size(); j++) {
                if (store.get(j).getWinRank() == 1)
                    win1Count++;
                else if (store.get(j).getWinRank() == 2)
                    win2Count++;
                if (!storeUuid.equals(store.get(j).getStoreUuid())) {
                    i = --j;
                    break;
                }
            }
            storeList.get(storeList.size() - 1).setWin1stCount(win1Count);
            storeList.get(storeList.size() - 1).setWin2stCount(win2Count);
            if (j >= store.size())
                break;
        }
        return storeList;
    }

    private RequestStoreFilterDTO checkFilterValid(RequestStoreFilterDTO requestDTO) {
        if (requestDTO.getLottoID() < 1 || requestDTO.getLottoID() > 5)
            requestDTO.setLottoID(1);

        if (requestDTO.getLimit() == 0)
            requestDTO.setLimit(-1);

        if (requestDTO.getOffset() == -1)
            requestDTO.setOffset(0);
        else

        if (requestDTO.getLimit() != -1 && requestDTO.getOffset() > requestDTO.getLimit()) {
            int temp = requestDTO.getOffset();
            requestDTO.setOffset(requestDTO.getLimit());
            requestDTO.setLimit(temp);
        }
        if (null == requestDTO.getFilter())
            requestDTO.setFilter("");
        return requestDTO;
    }

    private List<Store> calculateDistance(Double userLatitude, Double userLongitude, List<Store> stores) {
        for(int i=0; i < stores.size(); i++){
            stores.get(i).setDistance(Util.getDistance(userLatitude, userLongitude,
                    stores.get(i).getStoreLatitude(), stores.get(i).getStoreLongitude()));
        }
        return stores;
    }

    private List<Store> removeClosedStore(List<Store> stores) {
        for(int i = 0; i < stores.size(); i++) {
            if (null!= stores.get(i).getStoreIsActivity() && stores.get(i).getStoreIsActivity() == true)
                stores.remove(i);
        }
        return stores;
    }
}
