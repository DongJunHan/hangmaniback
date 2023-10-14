package com.project.hangmani.board.model.entity;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardAttachment {
    private int attachmentNo;
    private String originalFileName;
    private String savedFileName;
    @Builder
    private BoardAttachment(int attachmentNo, String originalFileName, String savedFileName) {
        this.attachmentNo = attachmentNo;
        this.originalFileName = originalFileName;
        this.savedFileName = savedFileName;
    }
}
