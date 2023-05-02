package com.project.hangmani.domain;

import lombok.*;

import java.sql.Date;
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class StoreAttachment {
    private int attachmentNo;
    private String originalFileName;
    private String savedFileName;
    private long fileSize;
    private Date uploadDate;
    private String storeUuid;
}
