package com.project.hangmani.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class StoreAttachment {
    private int attachmentNo;
    private int reviewNo;
    private String originalFileName;
    private String renamedFileName;
    private Date uploadDate;
    private int downloadCount ;
    private String status; //첨부파일 삭제여부

    public StoreAttachment() {
    }

    public StoreAttachment(int attachmentNo, int reviewNo, String originalFileName,
                           String renamedFileName, Date uploadDate, int downloadCount, String status) {
        this.attachmentNo = attachmentNo;
        this.reviewNo = reviewNo;
        this.originalFileName = originalFileName;
        this.renamedFileName = renamedFileName;
        this.uploadDate = uploadDate;
        this.downloadCount = downloadCount;
        this.status = status;
    }
}
