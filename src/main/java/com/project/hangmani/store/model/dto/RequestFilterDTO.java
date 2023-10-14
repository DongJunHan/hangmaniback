package com.project.hangmani.store.model.dto;

import lombok.*;

import static com.project.hangmani.config.StoreFilterConst.AREA;
import static com.project.hangmani.config.StoreFilterConst.COORDINATES;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestFilterDTO {
    private String sido;
    private String sigugun;
    private Double startLatitude;
    private Double endLatitude;
    private Double startLongitude;
    private Double endLongitude;
    private Double userLatitude;
    private Double userLongitude;
    private Integer lottoID;
    private String filter;
    private Integer limit;
    private Integer offset;
    @Builder
    private RequestFilterDTO(String sido, String sigugun, Double startLatitude,
                             Double endLatitude, Double startLongitude, Double endLongitude,
                             Double userLatitude, Double userLongitude, Integer lottoID,
                             String filter, Integer limit, Integer offset) {
        this.sido = sido;
        this.sigugun = sigugun;
        this.startLatitude = startLatitude;
        this.endLatitude = endLatitude;
        this.startLongitude = startLongitude;
        this.endLongitude = endLongitude;
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
        this.lottoID = lottoID;
        this.filter = filter;
        this.limit = limit;
        this.offset = offset;
    }

    public String getRequestType() {
        if (sido != null && sigugun != null) {
            return AREA;
        }else {
            return COORDINATES;
        }
    }
}
