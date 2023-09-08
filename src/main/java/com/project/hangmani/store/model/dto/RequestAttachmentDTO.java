package com.project.hangmani.store.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestAttachmentDTO {
    private List<MultipartFile> filesData;
    private String storeUuid;
    private List<String> savedFileNames;
    private Double storeLatitude;
    private Double storeLongitude;
}
