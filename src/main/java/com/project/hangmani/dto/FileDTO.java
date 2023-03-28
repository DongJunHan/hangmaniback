package com.project.hangmani.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

public class FileDTO {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestStoreFileDTO {
        private MultipartFile fileData;
        private long fileSize;
        private String originalFileName;
        private String storeUUID;
        private Date uploadTime;
    }
}
