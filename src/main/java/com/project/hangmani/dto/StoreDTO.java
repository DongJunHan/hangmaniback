package com.project.hangmani.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public class StoreDTO {
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestStoreFilterDTO {
        @NotNull
        private String sido;
        @NotNull
        private String sigugun;
        private Double userLatitude;
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
        private Double storeLatitude;
        @NotNull
        private Double storeLongitude;
        private String storeBizNo;
        private String storeTelNum;
        private String storeMobileNum;

        private String storeOpenTime;
        private String storeCloseTime;
        private MultipartFile fileData;
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
        private Double storeLatitude;
        private Double storeLongitude;
        private String storeBizNo;
        private String storeTelNum;
        private String storeMobileNum;
        private String storeOpenTime;
        private String storeCloseTime;
        private Boolean storeIsActivity;
        private String storeSido;
        private  String storeSigugun;
        private List<String> fileUrlList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResponseStoreFilterDTO {
        private String storeUUID;
        private String storeName;
        private String storeAddress;
        private Double distance;
        private int firstWinCount;
        private int secondWinCount;
        private List<String> attachFileList;
        private List<String> lottoTypes;

    }
}
