package com.project.hangmani.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


public class StoreDTO {
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestStoresDTO {
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
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestStoreUpdateDTO {
        @NotBlank
        private String storeName;
        @NotBlank
        private String storeAddress;
        @NotNull
        private String storeLatitude;
        @NotNull
        private String storeLongitude;
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
