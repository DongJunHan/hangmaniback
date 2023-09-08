package com.project.hangmani.board.model.dto;

import lombok.*;

import java.sql.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDTO {
    private Integer boardNo;
    private String boardTitle;
    private String boardContent;
    private String boardWriter;
    private Date createAt;
    private Date updateAt;
    private Boolean isDelete;
}
