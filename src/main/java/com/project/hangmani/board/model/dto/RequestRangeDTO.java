package com.project.hangmani.board.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestRangeDTO {
    private Integer limit;
    private Integer offset;
    @Builder
    private RequestRangeDTO(Integer limit, Integer offset) {
        this.limit = limit;
        this.offset = offset;
    }
}
