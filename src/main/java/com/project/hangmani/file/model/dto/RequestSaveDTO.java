package com.project.hangmani.file.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestSaveDTO {
    private List<MultipartFile> attachFiles;
    @Builder
    private RequestSaveDTO(List<MultipartFile> attachFiles) {
        this.attachFiles = attachFiles;
    }
}
