package com.project.hangmani.store.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestAttachmentDTO {
    private List<MultipartFile> attachFiles;
    private String storeUuid;
    private List<String> savedFileNames;
    private Double storeLatitude;
    private Double storeLongitude;
    @Builder
    private RequestAttachmentDTO(List<MultipartFile> attachFiles, String storeUuid,
                                 List<String> savedFileNames, Double storeLatitude, Double storeLongitude) {
        this.attachFiles = attachFiles;
        this.storeUuid = storeUuid;
        this.savedFileNames = savedFileNames;
        this.storeLatitude = storeLatitude;
        this.storeLongitude = storeLongitude;
    }
}
