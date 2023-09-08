package com.project.hangmani.board.model.dto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestNoDTO {
    private Integer boardNo;
    private Integer limit;
    private Integer offset;
}
