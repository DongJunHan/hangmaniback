package com.project.hangmani.dto;

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
    /*
    1. 시도
    2. 시군구
    3. 내 위도 경도
    4. 시작 위도 경도
    5. 끝 위도 경도
    6. 로또 종류
        1. default : 6/45
    7. 필터
        1. 거리순
            1. 내 위도 경도와 함께, 위도 경도 범위를 같이 파라미터로 보냄
        2. 1등 많이 순
            1. 같이 요청날린 로또 종류 기준
        3. 2등 많이 순
            1. 같이 요청날린 로또 종류 기준
     */
    public static class RequestStoreFilterDTO {
        @NotNull
        private String sido;
        @NotNull
        private String sigugun;
        @NotNull
        private Double userLatitude;
        @NotNull
        private Double userLongitude;
        private Double startLatitude;
        private Double endLatitude;
        private Double startLongitude;
        private Double endLongitude;
        private String lottoType;
        private String filter;
    }
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
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
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestStoreDeleteDTO {
        @NotBlank
        private String storeUuid;
        private Double latitude;
        private Double longitude;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestStoreChangeDTO {
        @NotBlank
        private String content;
        @NotNull
        private String userAgent;
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
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestStoreInsertDTO {
        @NotBlank
        private String storeName;
        @NotBlank
        private String storeAddress;
        @NotNull
        private String storeLatitude;
        @NotNull
        private String storeLongitude;
        private String storeBizNo;
        private String storeTelNum;
        private String storeMobileNum;

        private String storeOpenTime;
        private String storeCloseTime;
        private byte[] fileData;
        private long fileSize;
        private String originalFileName;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResponseStoreChangeDTO {
        private String result;
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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResponseStoreFilterDTO {
        private String storeUUID;
        private String storeName;
        private Double distance;
        private List<String> lottoTypes;
        private String storeAddress;
        private int firstWinCount;
        private int secondWinCount;
        private String attachFile;
    }
}
