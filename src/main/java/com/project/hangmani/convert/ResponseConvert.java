package com.project.hangmani.convert;

import com.project.hangmani.domain.Board;
import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.BoardDTO.ResponseBoardDTO;
import com.project.hangmani.dto.BoardDTO.ResponseDeleteDTO;
import com.project.hangmani.dto.StoreDTO.ResponseStoreDTO;

public class ResponseConvert {
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
                .build();
    }
}
