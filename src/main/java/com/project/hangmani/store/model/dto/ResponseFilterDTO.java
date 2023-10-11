package com.project.hangmani.store.model.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseFilterDTO {
    private String storeUuid;
    private String storeName;
    private String storeAddress;
    private Double distance;
    private Map<Integer, int[]> winCounts;
    private List<AttachmentDTO> attachFileList;
    private List<String> lottoTypes;
    private Double storeLatitude;
    private Double storeLongitude;
    @Builder
    private ResponseFilterDTO(String storeUuid, String storeName, String storeAddress,
                              Double distance, Map<Integer, int[]> winCounts,
                              List<AttachmentDTO> attachFileList, List<String> lottoTypes,
                              Double storeLatitude, Double storeLongitude) {
        this.storeUuid = storeUuid;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.distance = distance;
        this.winCounts = winCounts;
        this.attachFileList = attachFileList;
        this.lottoTypes = lottoTypes;
        this.storeLatitude = storeLatitude;
        this.storeLongitude = storeLongitude;
    }
}
