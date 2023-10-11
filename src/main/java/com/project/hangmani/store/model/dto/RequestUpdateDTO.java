package com.project.hangmani.store.model.dto;

import com.project.hangmani.store.model.entity.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestUpdateDTO {
    @NotBlank
    private String storeName;
    @NotBlank
    private String storeAddress;
    @NotNull
    private Double storeLatitude;
    @NotNull
    private Double storeLongitude;
    @NotBlank
    private String storeBizNo;
    @NotBlank
    private String storeTelNum;
    @NotBlank
    private String storeMobileNum;

    private LocalDateTime storeOpenTime;
    private LocalDateTime storeCloseTime;
    private Boolean storeIsActivity;
    private String storeSido;
    private  String storeSigugun;
    @Builder
    private RequestUpdateDTO(String storeName, String storeAddress, Double storeLatitude,
                             Double storeLongitude, String storeBizNo, String storeTelNum,
                             String storeMobileNum, LocalDateTime storeOpenTime, LocalDateTime storeCloseTime,
                             Boolean storeIsActivity, String storeSido, String storeSigugun) {
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

    public Store of() {
        return Store.builder()
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
                .build();
    }
}
