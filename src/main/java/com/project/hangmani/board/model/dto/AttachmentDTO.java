package com.project.hangmani.board.model.dto;

import lombok.*;

import java.sql.Date;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttachmentDTO {
    private Integer attachmentNo;
    private String originalFileName;
    private String savedFileName;
    private Long fileSize;
    private Date uploadDate;
    private Byte[] fileData;
}
