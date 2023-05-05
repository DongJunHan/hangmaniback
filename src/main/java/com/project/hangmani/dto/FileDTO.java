package com.project.hangmani.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestStoreFileDTO {
        private List<MultipartFile> filesData;
        private String storeUUID;
        private List<String> savedFileNames;
        private Double storeLatitude;
        private Double storeLongitude;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseStoreFileDTO {
        private List<String> domainUrls;
        private String storeUUID;
    }
}
