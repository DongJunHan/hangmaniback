package com.project.hangmani.board.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDeleteDTO {
    @NotNull(message = "please boardNo insert")
    private Integer boardNo;
    @NotNull(message = "please boardWriter insert")
    private String boardWriter;
}
