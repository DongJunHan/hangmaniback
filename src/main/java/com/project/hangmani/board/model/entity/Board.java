package com.project.hangmani.board.model.entity;

import lombok.*;

import java.sql.Date;


@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Board {
    private int boardNo;
    private String boardTitle;
    private String boardContent;
    private String boardWriter;
    private Date createAt;
    private Date updateAt;
    private boolean isDelete;

}
