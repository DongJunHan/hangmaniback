package com.project.hangmani.file.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentDTO {
    private String originalFileName;
    private String savedFileName;
    @Builder
    private AttachmentDTO(String originalFileName, String savedFileName) {
        this.originalFileName = originalFileName;
        this.savedFileName = savedFileName;
    }
}
