package com.project.hangmani.board.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestWriterDTO {
    private Integer boardNo;
    private String boardWriter;
    private Integer limit;
    private Integer offset;
    @Builder
    private RequestWriterDTO(Integer boardNo, String boardWriter, Integer limit, Integer offset) {
        this.boardNo = boardNo;
        this.boardWriter = boardWriter;
        this.limit = limit;
        this.offset = offset;
    }
}
