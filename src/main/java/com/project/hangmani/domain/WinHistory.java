package com.project.hangmani.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class WinHistory {
    private String storeUuid;
    private Integer lottoId;
    private Integer winRound;
    private Integer winRank;

    public WinHistory() {
    }

    public WinHistory(String storeUuid, Integer lottoId,
                      Integer winRound, Integer winRank) {
        this.storeUuid = storeUuid;
        this.lottoId = lottoId;
        this.winRound = winRound;
        this.winRank = winRank;
    }
}
