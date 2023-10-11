package com.project.hangmani.store.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDTO {
    private String storeUuid;
    private String storeName;
    private String storeAddress;
    private Double storeLatitude;
    private Double storeLongitude;
    private String storeBizNo;
    private String storeTelNum;
    private String storeMobileNum;
    private LocalDateTime storeOpenTime;
    private LocalDateTime storeCloseTime;
    private Boolean storeIsActivity;
    private String storeSido;
    private String storeSigugun;
    private List<AttachmentDTO> fileUrlList;
    private List<String> lottoTypes;
    private Map<Integer, int[]> winCounts;
    @Builder
    private ResponseDTO(String storeUuid, String storeName, String storeAddress,
                        Double storeLatitude, Double storeLongitude, String storeBizNo,
                        String storeTelNum, String storeMobileNum, LocalDateTime storeOpenTime,
                        LocalDateTime storeCloseTime, Boolean storeIsActivity, String storeSido,
                        String storeSigugun, List<AttachmentDTO> fileUrlList, List<String> lottoTypes,
                        Map<Integer, int[]> winCounts) {
        this.storeUuid = storeUuid;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeLatitude = storeLatitude;
        this.storeLongitude = storeLongitude;
        this.storeBizNo = storeBizNo;
        this.storeTelNum = storeTelNum;
        this.storeMobileNum = storeMobileNum;
        this.storeOpenTime = storeOpenTime;
        this.storeCloseTime = storeCloseTime;
        this.storeIsActivity = storeIsActivity;
        this.storeSido = storeSido;
        this.storeSigugun = storeSigugun;
        this.fileUrlList = fileUrlList;
        this.lottoTypes = lottoTypes;
        this.winCounts = winCounts;
    }
}
