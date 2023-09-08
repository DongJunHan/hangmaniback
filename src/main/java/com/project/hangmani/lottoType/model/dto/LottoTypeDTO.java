package com.project.hangmani.lottoType.model.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LottoTypeDTO {
        private String storeUuid;
        private Integer lottoId;
        private String lottoName;

}
