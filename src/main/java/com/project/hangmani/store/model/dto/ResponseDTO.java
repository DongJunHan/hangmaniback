package com.project.hangmani.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDTO {
    private String storeUuid;
    private String storeName;
    private String storeAddress;
    private Double storeLatitude;
    private Double storeLongitude;
    private String storeBizNo;
    private String storeTelNum;
    private String storeMobileNum;
    private String storeOpenTime;
    private String storeCloseTime;
    private Boolean storeIsActivity;
    private String storeSido;
    private String storeSigugun;
    private List<String> fileUrlList;
    private List<String> lottoTypes;
    private Map<Integer, int[]> winCounts;


}
