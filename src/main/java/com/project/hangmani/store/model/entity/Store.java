package com.project.hangmani.store.model.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {
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
    @Builder
    private Store(String storeUuid, String storeName, String storeAddress,
                  Double storeLatitude, Double storeLongitude, String storeBizNo,
                  String storeTelNum, String storeMobileNum, LocalDateTime storeOpenTime,
                  LocalDateTime storeCloseTime, Boolean storeIsActivity, String storeSido, String storeSigugun) {
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
    }
}
