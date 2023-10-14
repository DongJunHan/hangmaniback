package com.project.hangmani.lottoType.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LottoType {
    private String storeUuid;
    private int lottoId;
    private String lottoName;
    @Builder
    private LottoType(String storeUuid, int lottoId, String lottoName) {
        this.storeUuid = storeUuid;
        this.lottoId = lottoId;
        this.lottoName = lottoName;
    }
}
