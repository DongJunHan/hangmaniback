package com.project.hangmani.domain;

import lombok.*;

import java.sql.Date;


@Setter
@Getter
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

}
