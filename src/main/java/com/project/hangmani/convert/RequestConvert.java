package com.project.hangmani.convert;

import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.domain.Board;
import com.project.hangmani.domain.Store;
import com.project.hangmani.domain.StoreAttachment;
import com.project.hangmani.dto.BoardDTO.RequestBoardInsertDTO;
import com.project.hangmani.dto.FileDTO.RequestStoreFileDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoreInsertDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoreUpdateDTO;
import com.project.hangmani.dto.UserDTO.RequestInsertUserDTO;
import com.project.hangmani.util.ConvertData;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.Map;

public class RequestConvert {
    private final ConvertData convertData;
    public RequestConvert(PropertiesValues propertiesValues) {
        this.convertData = new ConvertData(propertiesValues);
    }
    /**
     * boardInsert
     * convert DTO to Entity
     * @param boardDTO
     * @return
     */
    public Board convertEntity(RequestBoardInsertDTO boardDTO) {
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

    public RequestInsertUserDTO convertDTO(Map<String, Object> tokenInput, Map<String, Object> userInfo,
                                           String oAuthType) {
        Date refreshTokenExpiresIn = this.convertData.addSecondsCurrentDate((int) tokenInput.get("refresh_token_expires_in"));
        String oAuthID = userInfo.get("id").toString();
        Map<String, Object> scope = (Map<String, Object>) userInfo.get("kakao_account");
        return RequestInsertUserDTO.builder()
                .oAuthID(oAuthID)
                .refreshToken((String)tokenInput.get("refresh_token"))
                .refreshTokenExpire(refreshTokenExpiresIn)
                .age(scope.get("age_range").toString())
                .email(scope.get("email").toString())
                .gender(scope.get("gender").toString())
                .oAuthType(oAuthType)
                .build();

    }

    public RequestStoreFileDTO convertDTO(RequestStoreInsertDTO insertDTO, String uuid) {
        return RequestStoreFileDTO.builder()
                .filesData(insertDTO.getFilesData())
                .storeUuid(uuid)
                .build();
    }

    public StoreAttachment convertDTO(MultipartFile file, String savedFileName, String storeUuid, Date uploadTime){
        return StoreAttachment.builder()
                .storeUuid(storeUuid)
                .savedFileName(savedFileName)
                .originalFileName(file.getOriginalFilename())
                .uploadDate(uploadTime)
                .fileSize(file.getSize())
                .build();
    }
}
