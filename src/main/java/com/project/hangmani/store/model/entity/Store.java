package com.project.hangmani.store.model.entity;

import lombok.*;


@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {
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
    private String storeSigugun;
}
