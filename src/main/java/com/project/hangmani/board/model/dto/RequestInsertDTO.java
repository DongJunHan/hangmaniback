package com.project.hangmani.board.model.dto;

import com.project.hangmani.board.model.entity.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestInsertDTO {
    @NotBlank(message = "boardTitle insert")
    private String boardTitle;
    @NotNull(message = "boardContent insert")
    private String boardContent;
    @NotNull(message = "boardWriter insert")
    private String boardWriter;

    public Board convertToEntity(RequestInsertDTO dto) {
        return Board.builder()
                .boardTitle(dto.getBoardTitle())
                .boardContent(dto.getBoardContent())
                .boardWriter(dto.getBoardWriter())
                .build();
    }
}