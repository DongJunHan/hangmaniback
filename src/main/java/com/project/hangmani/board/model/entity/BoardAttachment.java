package com.project.hangmani.board.model.entity;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BoardAttachment {
    private int attachmentNo;
    private String originalFileName;
    private String savedFileName;
}
