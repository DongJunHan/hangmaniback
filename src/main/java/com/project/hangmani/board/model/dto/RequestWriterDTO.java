package com.project.hangmani.board.model.dto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestWriterDTO {
    private Integer boardNo;
    private String boardWriter;
    private Integer limit;
    private Integer offset;
}
