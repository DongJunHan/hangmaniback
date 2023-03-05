package com.project.hangmani.dto;

import com.project.hangmani.domain.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

public class StoreDTO {
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestStoreDTO {
        @NotNull
        private Double startLatitude;
        @NotNull
        private Double endLatitude;
        @NotNull
        private Double startLongitude;
        @NotNull
        private Double endLongitude;
        private int limit;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResponseStoreDTO {
        private String storeUuid;
        private String storeName;
        private String storeAddress;
        private String storeLatitude;
        private String storeLongitude;
        private String storeBizNo;
        private String storeTelNum;
        private String storeMobileNum;
        private String storeOpenTime;
        private String storeCloseTime;
        private Boolean storeIsActivity;
        private String storeSido;
        private  String storeSigugun;

    }
}
