package com.project.hangmani.board.model.entity;

import lombok.*;

import java.sql.Date;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Board {
    private int boardNo;
    private String boardTitle;
    private String boardContent;
    private String boardWriter;
    private Date createAt;
    private Date updateAt;
    private boolean isDelete;
    @Builder
    private Board(int boardNo, String boardTitle, String boardContent,
                  String boardWriter, Date createAt, Date updateAt, boolean isDelete) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardWriter = boardWriter;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.isDelete = isDelete;
    }
}
