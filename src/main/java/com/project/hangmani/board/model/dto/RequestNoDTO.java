package com.project.hangmani.board.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestNoDTO {
    private Integer boardNo;
    private Integer limit;
    private Integer offset;
    @Builder
    private RequestNoDTO(Integer boardNo, Integer limit, Integer offset) {
        this.boardNo = boardNo;
        this.limit = limit;
        this.offset = offset;
    }
}
