package com.project.hangmani.convert;

import com.project.hangmani.domain.Board;
import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.BoardDTO.RequestBoardDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoreUpdateDTO;

public class RequestConvert {
    /**
     * boardInsert
     * convert DTO to Entity
     * @param boardDTO
     * @return
     */
    public Board convertEntity(RequestBoardDTO boardDTO) {
        String boardWriter = boardDTO.getBoardWriter();
        String content = boardDTO.getBoardContent();
        String title = boardDTO.getBoardTitle();

        Board board = new Board();
        board.setBoardWriter(boardWriter);
        board.setBoardContent(content);
        board.setBoardTitle(title);
        return board;
    }

    public Store convertEntity(RequestStoreUpdateDTO storeDTO) {
        Store store = new Store();
        store.setStoreAddress(storeDTO.getStoreAddress());
        store.setStoreName(storeDTO.getStoreName());
        store.setStoreLatitude(storeDTO.getStoreLatitude());
        store.setStoreLongitude(storeDTO.getStoreLongitude());
        store.setStoreBizNo(storeDTO.getStoreBizNo());
        store.setStoreTelNum(storeDTO.getStoreTelNum());
        store.setStoreMobileNum(storeDTO.getStoreMobileNum());
        store.setStoreOpenTime(storeDTO.getStoreOpenTime());
        store.setStoreCloseTime(storeDTO.getStoreCloseTime());
        store.setStoreIsActivity(storeDTO.getStoreIsActivity());
        store.setStoreSido(storeDTO.getStoreSido());
        store.setStoreSigugun(storeDTO.getStoreSigugun());
        return store;
    }
}
