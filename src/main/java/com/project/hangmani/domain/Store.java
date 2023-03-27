package com.project.hangmani.domain;

import jakarta.validation.Valid;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Store {
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
    private String storeSigugun;
    private Integer win2stCount;
    private Integer win1stCount;
    private String lottoName;
}
