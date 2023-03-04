package com.project.hangmani.domain;

import lombok.*;

import java.sql.Date;


@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Board {
    private int boardNo;
    private String title;
    private String content;
    private String boardWriter;
    private Date createAt;
    private Date updateAt;

    public Board() {
    }

    public Board(int boardNo, String title, String content,
                 String boardWriter, Date createAt, Date updateAt) {
        this.boardNo = boardNo;
        this.title = title;
        this.content = content;
        this.boardWriter = boardWriter;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
