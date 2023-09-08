package com.project.hangmani.store.model.dto;

import com.project.hangmani.config.StoreFilterConst;
import lombok.*;

import static com.project.hangmani.config.StoreFilterConst.AREA;
import static com.project.hangmani.config.StoreFilterConst.COORDINATES;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
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


    public String getRequestType() {
        if (sido != null && sigugun != null) {
            return AREA;
        }else {
            return COORDINATES;
        }
    }
}
