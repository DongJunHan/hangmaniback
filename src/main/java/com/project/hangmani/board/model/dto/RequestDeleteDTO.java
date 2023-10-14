package com.project.hangmani.board.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestDeleteDTO {
    @NotNull(message = "please boardNo insert")
    private Integer boardNo;
    @NotNull(message = "please boardWriter insert")
    private String boardWriter;
    @Builder
    private RequestDeleteDTO(Integer boardNo, String boardWriter) {
        this.boardNo = boardNo;
        this.boardWriter = boardWriter;
    }
}
