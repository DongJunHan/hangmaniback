package com.project.hangmani.store.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDTO {
    private String storeUuid;
    private String storeName;
    private String storeAddress;
    private String storeBizNo;
    private String storeTelNum;
    private String storeMobileNum;
    private String storeOpenTime;
    private String storeCloseTime;
    private String storeSido;
    private String storeSigugun;
    private Double storeLatitude;
    private Double storeLongitude;
    private Double distance;
    private Boolean storeIsActivity;
    private Integer winRank;
    private Integer winRound;
    private String lottoTypes;
    private String savedFileNames;
    private Integer lottoId;
    private String lottoNames;
    private Integer win1stCount;
    private Integer win2stCount;

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

    public ResponseDTO convertResponseDTO() {
//        String savedFileNames = this.getSavedFileNames();
//        List<String> fileUrls = new ArrayList<>();
//        List<String> lottoType = new ArrayList<>();
//        if (savedFileNames != null) {
//            String[]  split = savedFileNames.split(",");
//            Collections.addAll(fileUrls, split);
//        }
//        if (this.getLottoName() != null){
//            String[] split = this.getLottoName().split(",");
//            Collections.addAll(lottoType, split);
//        }
        return ResponseDTO.builder()
                .storeUuid(this.getStoreUuid())
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
//                .fileUrlList(this.get)
//                .lottoTypes(this.getLottoTypes())
//                .winCounts(this.getWinCounts())
                .build();
    }

}
