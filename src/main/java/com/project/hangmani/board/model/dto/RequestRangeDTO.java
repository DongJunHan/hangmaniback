package com.project.hangmani.board.model.dto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestRangeDTO {
    private Integer limit;
    private Integer offset;
}
