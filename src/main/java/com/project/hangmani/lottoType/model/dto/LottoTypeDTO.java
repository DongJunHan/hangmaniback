package com.project.hangmani.lottoType.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class LottoTypeDTO {
        private String storeUuid;
        private Integer lottoId;
        private String lottoName;
        @Builder
        private LottoTypeDTO(String storeUuid, Integer lottoId, String lottoName) {
                this.storeUuid = storeUuid;
                this.lottoId = lottoId;
                this.lottoName = lottoName;
        }
}
