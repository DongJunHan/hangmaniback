package com.project.hangmani.convert;

import com.project.hangmani.domain.Board;
import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.BoardDTO.ResponseBoardDTO;
import com.project.hangmani.dto.BoardDTO.ResponseDeleteDTO;
import com.project.hangmani.dto.StoreDTO.ResponseStoreDTO;
import com.project.hangmani.dto.StoreDTO.ResponseStoreFilterDTO;
import com.project.hangmani.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResponseConvert {
    private final Util util;

    public ResponseConvert(Util util) {
        this.util = util;
    }

    public ResponseDeleteDTO convertResponseDTO(int rowNum) {
        return ResponseDeleteDTO.builder().rowNum(rowNum).build();
    }
    public ResponseBoardDTO convertResponseDTO(Board board) {
        return ResponseBoardDTO.builder()
                .boardNo(board.getBoardNo())
                .boardContent(board.getBoardContent())
                .boardWriter(board.getBoardWriter())
                .boardTitle(board.getBoardTitle())
                .createAt(board.getCreateAt())
                .updateAt(board.getUpdateAt())
                .build();
    }
    public ResponseStoreDTO convertResponseDTO(Store store) {
        String savedFileNames = store.getSavedFileNames();
        List<String> fileUrls = new ArrayList<>();
        List<String> lottoType = new ArrayList<>();
        if (savedFileNames != null) {
            String[]  split = savedFileNames.split(",");
            Collections.addAll(fileUrls, split);
        }
        if (store.getLottoName() != null){
            String[] split = store.getLottoName().split(",");
            Collections.addAll(lottoType, split);
        }
        return ResponseStoreDTO.builder()
                .storeUuid(store.getStoreUuid())
                .storeName(store.getStoreName())
                .storeAddress(store.getStoreAddress())
                .storeLatitude(store.getStoreLatitude())
                .storeLongitude(store.getStoreLongitude())
                .storeBizNo(store.getStoreBizNo())
                .storeTelNum(store.getStoreTelNum())
                .storeMobileNum(store.getStoreMobileNum())
                .storeOpenTime(store.getStoreOpenTime())
                .storeCloseTime(store.getStoreCloseTime())
                .storeIsActivity(store.getStoreIsActivity())
                .storeSido(store.getStoreSido())
                .storeSigugun(store.getStoreSigugun())
                .fileUrlList(fileUrls)
                .lottoTypes(lottoType)
                .firstWinCount(store.getWin1stCount())
                .secondWinCount(store.getWin2stCount())
                .build();
    }
/*
        private String storeUUID;
        private String storeName;
        private String storeAddress;
        private Double distance;
        private int firstWinCount;
        private int secondWinCount;
        private List<String> attachFileList;
        private List<String> lottoTypes;
 */

    public List<ResponseStoreFilterDTO> convertResponseDTO(List<Store> storeInfos) {
        List<ResponseStoreFilterDTO> result = new ArrayList<>();
        for (Store storeInfo:storeInfos) {
            //split saved file name
            List<String> savedFileList = new ArrayList<>();
            String[] savedFiles = {};
            //store attachment null check
            if (null != storeInfo.getSavedFileNames())
                savedFiles  = storeInfo.getSavedFileNames().split(",");
            for(String s: savedFiles){
                savedFileList.add(s.trim());
            }
            //split lotto type
            List<String> lottoTypeList = new ArrayList<>();
            String[] lottoTypes = {};
            //lotto type null check
            if (null != storeInfo.getLottoName())
                lottoTypes = storeInfo.getLottoName().split(",");
            for(String l: lottoTypes){
                lottoTypeList.add(l.trim());
            }
            result.add(ResponseStoreFilterDTO.builder()
                    .storeUuid(storeInfo.getStoreUuid())
                    .storeName(storeInfo.getStoreName())
                    .storeAddress(storeInfo.getStoreAddress())
                    .distance(storeInfo.getDistance())
                    .firstWinCount(storeInfo.getWin1stCount())
                    .secondWinCount(storeInfo.getWin2stCount())
                    .attachFileList(savedFileList)
                    .lottoTypes(lottoTypeList)
                    .build());
        }
        return result;
    }
}
