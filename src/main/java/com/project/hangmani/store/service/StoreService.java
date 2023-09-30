package com.project.hangmani.store.service;

import com.project.hangmani.convert.ResponseConvert;
import com.project.hangmani.exception.AlreadyExistStore;
import com.project.hangmani.exception.FailDeleteData;
import com.project.hangmani.exception.FailUpdateStore;
import com.project.hangmani.exception.NotFoundStore;
import com.project.hangmani.lottoType.model.dto.LottoTypeDTO;
import com.project.hangmani.file.service.FileService;
import com.project.hangmani.store.model.dto.*;
import com.project.hangmani.store.repository.StoreRepository;
import com.project.hangmani.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final FileService fileService;
    private final StoreFilterFactory storeFilterFactory;
    private StoreFilter storeFilter;

    private final ResponseConvert responseConvert;
    /**
     * storeuuid를 가지고 상점정보 1개를 가져오는 함수.
     * 실제로는 쿼리를 세 번 호출해서 정보를 가지고 온다
     * @param storeUuid
     * @return
     */
    public ResponseDTO getStoreInfo(String storeUuid) {
        StoreDTO store;
        List<StoreDTO> stores = storeRepository.getByUuid(storeUuid);
        stores = postProcessFilterData(stores);
        if (stores.isEmpty())
            throw new NotFoundStore("상점정보를 찾을 수 없습니다.");
        List<LottoTypeDTO> lottoTypes = storeRepository.getLottoNameByUuid(storeUuid);
        stores = postProcessFilterData(lottoTypes, stores);
        List<AttachmentDTO> storeAttachments = storeRepository.getAttachmentByUuid(storeUuid);
        stores = postProcessAttachmentData(storeAttachments, stores);
        if (stores.isEmpty())
            throw new NotFoundStore("상점정보를 찾을 수 없습니다.");
        store = stores.get(0);

        return store.convertResponseDTO();
    }
    @Transactional
    public ResponseDTO updateStoreInfo(String storeUuid, RequestUpdateDTO requestUpdateDTO) {
        List<StoreDTO> stores = null;
        StoreDTO store = null;
        //check store exist
        if (checkNotExistStore(storeUuid)) {
            throw new NotFoundStore();
        }
        //update store info
        int ret = storeRepository.update(storeUuid, requestUpdateDTO.of());
        if (ret == 0) {
            throw new FailUpdateStore();
        }
        stores = storeRepository.getByUuid(storeUuid);
        store = stores.get(0);

        return store.convertResponseDTO();
    }
    @Transactional
    public ResponseDTO insert(RequestInsertDTO requestStoreDTO) throws IOException {
        //check already exist
        List<StoreDTO> findStore = storeRepository.getByStoreNameAndCoordinates(requestStoreDTO.convertToEntity());
        if (findStore.size() > 0) {
            throw new AlreadyExistStore();
        }

        String storeUuid = storeRepository.insert(requestStoreDTO.convertToEntity());
        List<AttachmentDTO> attachFiles = fileService.saveAttachment(storeUuid, requestStoreDTO.convertSaveDTO());
        storeRepository.insertAttachFiles(attachFiles.stream().map(e->e.toEntity()).toList());
        //get store info
        List<StoreDTO> stores = storeRepository.getByUuid(storeUuid);
        StoreDTO storeDTO = stores.get(0);
        return storeDTO.convertResponseDTO();
    }
    @Transactional
    public int delete(RequestDeleteDTO requestDTO) {
        //check uuid
        if (checkNotExistStore(requestDTO.getStoreUuid())) {
            throw new NotFoundStore();
        }

        //delete
        int ret = storeRepository.delete(requestDTO.getStoreUuid());
        if (ret == 0) {
            throw new FailDeleteData();
        }
        return ret;
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

    /**
     * 위도/경도로 상점 정보들 가져오기
     * @param requestDTO
     * @return
     */
    public List<ResponseFilterDTO> getStoreInfo(RequestFilterDTO requestDTO) {
        return getStoreInfoExecute(requestDTO);
    }

    /**
     * TODO
     * 당첨내역을 가지고 와서 당첨횟수를 세지말고
     * 처음부터 DB에 당첨횟수를 저장 해보자.
     * 스프링 배치를 돌려가면서 로또 당첨 횟수 스크래핑하고, DB업데이트
     */
    private List<ResponseFilterDTO> getStoreInfoExecute(RequestFilterDTO requestDTO) {
        storeFilter = storeFilterFactory.getInstance(requestDTO.getRequestType());

        requestDTO = validFilterParam(requestDTO);
        List<StoreDTO> storeInfos;
        storeInfos = storeFilter.getWithWinHistory(requestDTO);
        storeInfos = postProcessFilterData(storeInfos);
        List<LottoTypeDTO> lottoTypes = storeFilter.getLottoName(requestDTO);
        storeInfos = postProcessFilterData(lottoTypes, storeInfos);
        List<AttachmentDTO> storeAttachments = storeFilter.getAttachment(requestDTO);
        storeInfos = postProcessAttachmentData(storeAttachments, storeInfos);

        storeInfos = postProcessing(storeInfos, requestDTO.getFilter(), requestDTO.getLimit(), requestDTO.getOffset());
        return responseConvert.convertResponseDTO(storeInfos);
    }

//    /**
//     * 시도/시구군으로 상점 정보 가져오기
//     * @param requestDTO
//     * @return
//     */
//    public List<ResponseFilterDTO> getStoreInfo(RequestFilterDTO requestDTO) {
//        List<StoreDTO> storeInfos;
//
//        requestDTO = storeFilter.validFilterParam(requestDTO);
//        storeInfos = storeRepository.getWithWinHistoryByArea(requestDTO);
//        storeInfos = postProcessFilterData(storeInfos);
//        List<LottoTypeDTO> lottoTypes = storeRepository.getLottoNameByArea(requestDTO);
//        storeInfos = postProcessFilterData(lottoTypes, storeInfos);
//        List<AttachmentDTO> storeAttachments = storeRepository.getAttachmentByArea(requestDTO);
//        storeInfos = postProcessAttachmentData(storeAttachments, storeInfos);
//        storeInfos = postProcessing(storeInfos, requestDTO.getFilter(), requestDTO.getLimit(), requestDTO.getOffset());
//        return responseConvert.convertResponseDTO(storeInfos);
//    }

    /**
     * 함수가 정상적으로 실행되려면 uuid를 기준으로 정렬이 되어 있어야 함
     * @param storeAttachments
     * @param store
     * @return
     */
    private List<StoreDTO> postProcessAttachmentData(List<AttachmentDTO> storeAttachments, List<StoreDTO> store) {
        Map<String, List<com.project.hangmani.file.model.dto.AttachmentDTO>> storeAttachment = new HashMap<>();
        List<com.project.hangmani.file.model.dto.AttachmentDTO> list = null;
        String storeUuid = null;
        for (int i = 0; i < storeAttachments.size(); i++) {
            if(null == storeUuid || !storeUuid.equals(storeAttachments.get(i).getStoreUuid())) {
                storeUuid = storeAttachments.get(i).getStoreUuid();
                if (null == storeAttachments.get(i).getSavedFileName()) {
                    continue;
                }
                list = new ArrayList<>();
                list.add(new com.project.hangmani.file.model.dto.AttachmentDTO(
                        fileService.getFullPath(storeAttachments.get(i).getOriginalFileName()),
                        fileService.getFullPath(storeAttachments.get(i).getSavedFileName())));
                storeAttachment.put(storeUuid, list);
            } else if (storeUuid.equals(storeAttachments.get(i).getStoreUuid())){
                list.add(new com.project.hangmani.file.model.dto.AttachmentDTO(
                        fileService.getFullPath(storeAttachments.get(i).getOriginalFileName()),
                        fileService.getFullPath(storeAttachments.get(i).getSavedFileName())));
                storeAttachment.put(storeUuid, list);
            }
        }
        for (int i = 0; i < store.size(); i++) {
            store.get(i).setSavedFileNames(storeAttachment.get(store.get(i).getStoreUuid()));
        }
        return store;
    }

    /**
     * 함수가 정상적으로 실행되려면 uuid를 기준으로 정렬이 필요함
     * @param lottoTypes
     * @param store
     * @return
     */
    private List<StoreDTO> postProcessFilterData(List<LottoTypeDTO> lottoTypes, List<StoreDTO> store) {
        Map<String, List<String>> lottoType = new HashMap<>();
        String storeUuid = null;
        List<String> types = null;
        for (int i = 0; i < lottoTypes.size(); i++) {
            if(null == storeUuid || !storeUuid.equals(lottoTypes.get(i).getStoreUuid())) {
                types = new ArrayList<>();
                types.add(lottoTypes.get(i).getLottoName());
                storeUuid = lottoTypes.get(i).getStoreUuid();
                lottoType.put(storeUuid, types);
            } else if (storeUuid.equals(lottoTypes.get(i).getStoreUuid())){
                types.add(lottoTypes.get(i).getLottoName());
                lottoType.put(storeUuid, types);
            }
        }
        for (int i = 0; i < store.size(); i++) {
            store.get(i).setLottoNames(lottoType.get(store.get(i).getStoreUuid()));
        }
        return store;
    }

    /**
     * 함수가 정상적으로 실행되려면 uuid를 기준으로 정렬이 필요함
     * @param store
     * @return
     */
    private List<StoreDTO> postProcessFilterData(List<StoreDTO> store) {
        //TODO 각 로또별 당첨 횟수를 분리하도록 로직을 구현해야한다.
        List<StoreDTO> storeList = new ArrayList<>();
        String storeUuid;
        int win1Count;
        int win2Count;
        int j = 0;
        for (int i = 0; i < store.size(); i++) {
            storeUuid = store.get(i).getStoreUuid();
            win1Count = 0;
            win2Count = 0;
            storeList.add(StoreDTO.builder()
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

//    private RequestFilterDTO checkFilterValid(RequestFilterDTO requestDTO) {
//        if (requestDTO.getLottoID() < 1 || requestDTO.getLottoID() > 5)
//            requestDTO.setLottoID(1);
//
//        if (requestDTO.getLimit() == null || requestDTO.getLimit() == 0)
//            requestDTO.setLimit(-1);
//
//        if (requestDTO.getOffset() == null ||requestDTO.getOffset() == -1)
//            requestDTO.setOffset(0);
//
//        if (requestDTO.getLimit() != -1 && requestDTO.getOffset() > requestDTO.getLimit()) {
//            int temp = requestDTO.getOffset();
//            requestDTO.setOffset(requestDTO.getLimit());
//            requestDTO.setLimit(temp);
//        }
//        if (null == requestDTO.getFilter())
//            requestDTO.setFilter("");
//        return requestDTO;
//    }

    private List<StoreDTO> calculateDistance(Double userLatitude, Double userLongitude, List<StoreDTO> stores) {
        for(int i=0; i < stores.size(); i++){
            stores.get(i).setDistance(Util.getDistance(userLatitude, userLongitude,
                    stores.get(i).getStoreLatitude(), stores.get(i).getStoreLongitude()));
        }
        return stores;
    }

    private List<StoreDTO> removeClosedStore(List<StoreDTO> stores) {
        for(int i = 0; i < stores.size(); i++) {
            if (null!= stores.get(i).getStoreIsActivity() && stores.get(i).getStoreIsActivity() == true)
                stores.remove(i);
        }
        return stores;
    }

    private List<StoreDTO> postProcessing(List<StoreDTO> stores, String filter, int limit, int offset) {
        //check storeIsActivity
        stores = removeClosedStore(stores);
        //sort
        if (filter.equals("2st")) {
            Comparator<StoreDTO> second = Comparator.comparingInt(StoreDTO::getWin2stCount);
            Collections.sort(stores, second.reversed());
        } else if(filter.equals("1st")) {
            Comparator<StoreDTO> first = Comparator.comparingInt(StoreDTO::getWin1stCount);
            Collections.sort(stores, first.reversed());
        } else{
            Comparator<StoreDTO> distance = Comparator.comparingDouble(StoreDTO::getDistance);
            Collections.sort(stores, distance);
        }
        // limit
        if (limit > 0) {
            if (stores.size() < limit)
                limit = stores.size();
            stores = stores.stream().limit(limit).collect(Collectors.toList());
        }
        if (offset > 0){
            if (stores.size() < offset)
                offset = stores.size();
            for (int i = (stores.size() - offset); i < limit; i++){
                stores.remove(0);
            }
        }
        return stores;
    }

    public RequestFilterDTO validFilterParam(RequestFilterDTO requestDTO) {
        if (requestDTO.getLottoID() < 1 || requestDTO.getLottoID() > 5)
            requestDTO.setLottoID(1);

        if (requestDTO.getLimit() == null || requestDTO.getLimit() == 0)
            requestDTO.setLimit(-1);

        if (requestDTO.getOffset() == null ||requestDTO.getOffset() == -1)
            requestDTO.setOffset(0);

        if (requestDTO.getLimit() != -1 && requestDTO.getOffset() > requestDTO.getLimit()) {
            int temp = requestDTO.getOffset();
            requestDTO.setOffset(requestDTO.getLimit());
            requestDTO.setLimit(temp);
        }
        if (null == requestDTO.getFilter())
            requestDTO.setFilter("");
        return requestDTO;
    }

    private boolean checkNotExistStore(String storeUuid) {
        List<StoreDTO> store = storeRepository.getByUuid(storeUuid);
        if (store.size() == 0)
            return true;
        return false;
    }
}