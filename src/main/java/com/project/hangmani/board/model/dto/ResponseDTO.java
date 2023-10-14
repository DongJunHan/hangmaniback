package com.project.hangmani.board.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDTO {
    private Integer rowNum;
    @Builder
    private ResponseDTO(Integer rowNum) {
        this.rowNum = rowNum;
    }
}
