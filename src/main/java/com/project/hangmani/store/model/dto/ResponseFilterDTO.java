package com.project.hangmani.store.model.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResponseFilterDTO {
    private String storeUuid;
    private String storeName;
    private String storeAddress;
    private Double distance;
    private Map<Integer, int[]> winCounts;
    private List<String> attachFileList;
    private List<String> lottoTypes;
    private Double storeLatitude;
    private Double storeLongitude;
}
