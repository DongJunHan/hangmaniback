package com.project.hangmani.model.management;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;


@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Content {
    private int boardNo;
    private String title;
    private String content;
    private String boardWriter;
    private Date createDate;

    public Content() {
    }

    public Content(int boardNo, String title, String content,
                   String boardWriter, Date createDate) {
        this.boardNo = boardNo;
        this.title = title;
        this.content = content;
        this.boardWriter = boardWriter;
        this.createDate = createDate;
    }
}
