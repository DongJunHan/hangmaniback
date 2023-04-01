package com.project.hangmani.domain;

import lombok.*;

import java.sql.Date;

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
    private long fileSize;
    private Date uploadDate;
    private Byte[] fileData;

}
