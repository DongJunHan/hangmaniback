package com.project.hangmani.store.model.dto;

import com.project.hangmani.store.model.dto.AttachmentDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreDTO {
    private String storeUuid;
    private String storeName;
    private String storeAddress;
    private String storeBizNo;
    private String storeTelNum;
    private String storeMobileNum;
    private LocalDateTime storeOpenTime;
    private LocalDateTime storeCloseTime;
    private String storeSido;
    private String storeSigugun;
    private Double storeLatitude;
    private Double storeLongitude;
    private Double distance;
    private Boolean storeIsActivity;
    private Integer winRank;
    private Integer winRound;
    private String lottoTypes;
    private List<AttachmentDTO> savedFileNames;
    private Integer lottoId;
    private List<String> lottoNames;
    private Integer win1stCount;
    private Integer win2stCount;
    @Builder
    private StoreDTO(String storeUuid, String storeName, String storeAddress,
                     String storeBizNo, String storeTelNum, String storeMobileNum,
                     LocalDateTime storeOpenTime, LocalDateTime storeCloseTime, String storeSido,
                     String storeSigugun, Double storeLatitude, Double storeLongitude,
                     Double distance, Boolean storeIsActivity, Integer winRank,
                     Integer winRound, String lottoTypes, List<AttachmentDTO> savedFileNames,
                     Integer lottoId, List<String> lottoNames, Integer win1stCount, Integer win2stCount) {
        this.storeUuid = storeUuid;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeBizNo = storeBizNo;
        this.storeTelNum = storeTelNum;
        this.storeMobileNum = storeMobileNum;
        this.storeOpenTime = storeOpenTime;
        this.storeCloseTime = storeCloseTime;
        this.storeSido = storeSido;
        this.storeSigugun = storeSigugun;
        this.storeLatitude = storeLatitude;
        this.storeLongitude = storeLongitude;
        this.distance = distance;
        this.storeIsActivity = storeIsActivity;
        this.winRank = winRank;
        this.winRound = winRound;
        this.lottoTypes = lottoTypes;
        this.savedFileNames = savedFileNames;
        this.lottoId = lottoId;
        this.lottoNames = lottoNames;
        this.win1stCount = win1stCount;
        this.win2stCount = win2stCount;
    }
//    @Getter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class RequestStoreChangeDTO {
//        @NotBlank
//        private String content;
//        @NotNull
//        private String userAgent;
//    }
//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Builder
//    public static class ResponseStoreChangeDTO {
//        private String result;
//    }
    public ResponseDTO of() {
        return ResponseDTO.builder()
                .storeUuid(storeUuid)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .storeLatitude(storeLatitude)
                .storeLongitude(storeLongitude)
                .storeBizNo(storeBizNo)
                .storeTelNum(storeTelNum)
                .storeMobileNum(storeMobileNum)
                .storeOpenTime(storeOpenTime)
                .storeCloseTime(storeCloseTime)
                .storeIsActivity(storeIsActivity)
                .storeSido(storeSido)
                .storeSigugun(storeSigugun)
                .fileUrlList(savedFileNames)
                .lottoTypes(lottoNames)
//                .winCounts(this.getWinCounts())
                .build();
    }
}
