package com.project.hangmani.winHistory.model.entity;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WinHistory {
    private String storeUuid;
    private Integer lottoId;
    private Integer winRound;
    private Integer winRank;

    @Builder
    private WinHistory(String storeUuid, Integer lottoId,
                      Integer winRound, Integer winRank) {
        this.storeUuid = storeUuid;
        this.lottoId = lottoId;
        this.winRound = winRound;
        this.winRank = winRank;
    }
}
