package com.project.hangmani.convert;

import com.project.hangmani.domain.Board;
import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.BoardDTO.RequestBoardDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoreUpdateDTO;
import com.project.hangmani.dto.UserDTO.RequestInsertOAuthDTO;
import com.project.hangmani.dto.UserDTO.RequestInsertScopeDTO;
import com.project.hangmani.dto.UserDTO.RequestInsertUserDTO;
import com.project.hangmani.util.ConvertData;

import java.sql.Date;
import java.util.Map;

public class RequestConvert {
    private ConvertData convertData;
    public RequestConvert() {
        this.convertData = new ConvertData();
    }
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

    public RequestInsertUserDTO convertDTO(Map<String, Object> input, String id) {
        Date refreshTokenExpiresIn = this.convertData.addSecondsCurrentDate((int) input.get("refresh_token_expires_in"));
        return RequestInsertUserDTO.builder()
                .id(id)
                .refreshToken((String)input.get("refresh_token"))
                .refreshTokenExpire(refreshTokenExpiresIn)
                .build();

    }

    public RequestInsertScopeDTO convertDTO(Map<String, Object> input) {
        Map<String, Object> scope = (Map<String, Object>)input.get("kakao_account");

        return RequestInsertScopeDTO.builder()
                .age((String)scope.get("age_range"))
                .email((String)scope.get("email"))
                .gender((String)scope.get("gender"))
                .id(input.get("id").toString())
                .build();
    }
    public RequestInsertOAuthDTO convertDTO(String id, String oAuth) {
        return RequestInsertOAuthDTO.builder()
                .id(id)
                .oAuthType(oAuth)
                .build();
    }
}
