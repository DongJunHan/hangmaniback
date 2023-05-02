package com.project.hangmani.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestStoreFileDTO {
        private List<MultipartFile> filesData;
        private String storeUUID;
        private String fileUrl;
    }
}
