package com.project.hangmani.model.store;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
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
    private  String storeSigugun;
    public Store() {
    }

    public Store(String storeUuid, String storeName, String storeAddress, String storeLatitude,
                 String storeLongitude, String storeBizNo, String storeTelNum, String storeMobileNum,
                 String storeOpenTime, String storeCloseTime, Boolean storeIsActivity,
                 String storeSido, String storeSigugun) {
        this.storeUuid = storeUuid;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeLatitude = storeLatitude;
        this.storeLongitude = storeLongitude;
        this.storeBizNo = storeBizNo;
        this.storeTelNum = storeTelNum;
        this.storeMobileNum = storeMobileNum;
        this.storeOpenTime = storeOpenTime;
        this.storeCloseTime = storeCloseTime;
        this.storeIsActivity = storeIsActivity;
        this.storeSido = storeSido;
        this.storeSigugun = storeSigugun;
    }
}
