package com.project.hangmani.store.model.dto;

import com.project.hangmani.file.model.dto.AttachmentDTO;
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
    private List<AttachmentDTO> savedFileNames;
    private Integer lottoId;
    private List<String> lottoNames;
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
                .storeUuid(storeUuid)
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
                .fileUrlList(savedFileNames)
                .lottoTypes(lottoNames)
//                .winCounts(this.getWinCounts())
                .build();
    }

}
