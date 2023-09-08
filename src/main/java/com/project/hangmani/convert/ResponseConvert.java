package com.project.hangmani.convert;

import com.project.hangmani.board.model.dto.ResponseDTO;
import com.project.hangmani.board.model.dto.ResponseGetDTO;
import com.project.hangmani.board.model.entity.Board;
import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.store.model.dto.ResponseFilterDTO;
import com.project.hangmani.store.model.dto.StoreDTO;
import com.project.hangmani.util.Util;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class ResponseConvert {
    private Util util;

    public ResponseConvert(PropertiesValues propertiesValues) {
        this.util = new Util(propertiesValues);
    }

    public ResponseDTO convertResponseDTO(int rowNum) {
        return ResponseDTO.builder().rowNum(rowNum).build();
    }
    public ResponseGetDTO convertResponseDTO(Board board) {
        return ResponseGetDTO.builder()
                .boardNo(board.getBoardNo())
                .boardContent(board.getBoardContent())
                .boardWriter(board.getBoardWriter())
                .boardTitle(board.getBoardTitle())
                .createAt(board.getCreateAt())
                .updateAt(board.getUpdateAt())
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

    public List<ResponseFilterDTO> convertResponseDTO(List<StoreDTO> storeInfos) {
        List<ResponseFilterDTO> result = new ArrayList<>();
        for (StoreDTO storeInfo:storeInfos) {
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
            if (null != storeInfo.getLottoNames())
                lottoTypes = storeInfo.getLottoNames().split(",");
            for(String l: lottoTypes){
                lottoTypeList.add(l.trim());
            }
            result.add(ResponseFilterDTO.builder()
                    .storeUuid(storeInfo.getStoreUuid())
                    .storeName(storeInfo.getStoreName())
                    .storeAddress(storeInfo.getStoreAddress())
                    .distance(storeInfo.getDistance())
                    .storeLatitude(storeInfo.getStoreLatitude())
                    .storeLongitude(storeInfo.getStoreLongitude())
                    .attachFileList(savedFileList)
                    .lottoTypes(lottoTypeList)
                    .build());
        }
        return result;
    }
}
