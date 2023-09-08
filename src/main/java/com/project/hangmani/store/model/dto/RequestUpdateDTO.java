package com.project.hangmani.store.model.dto;

import com.project.hangmani.store.model.entity.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    private String storeOpenTime;
    private String storeCloseTime;
    private Boolean storeIsActivity;
    private String storeSido;
    private  String storeSigugun;

    public Store of() {
        return Store.builder()
                .storeName(this.getStoreName())
                .storeAddress(this.getStoreAddress())
                .storeLatitude(this.getStoreLatitude())
                .storeLongitude(this.getStoreLongitude())
                .storeBizNo(this.getStoreBizNo())
                .storeTelNum(this.getStoreTelNum())
                .storeMobileNum(this.getStoreMobileNum())
                .storeOpenTime(this.getStoreOpenTime())
                .storeCloseTime(this.getStoreCloseTime())
                .storeIsActivity(this.getStoreIsActivity())
                .storeSido(this.getStoreSido())
                .storeSigugun(this.getStoreSigugun())
                .build();
    }
}
