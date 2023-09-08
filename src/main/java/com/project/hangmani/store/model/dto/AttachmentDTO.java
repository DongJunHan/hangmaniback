package com.project.hangmani.store.model.dto;

import lombok.*;

import java.sql.Date;

@Getter
@ToString
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDTO {
    private Integer attachmentNo;
    private String originalFileName;
    private String savedFileName;
    private Long fileSize;
    private Date uploadDate;
    private String storeUuid;
}