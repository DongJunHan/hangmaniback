package com.project.hangmani.store.model.dto;

import com.project.hangmani.store.model.entity.StoreAttachment;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentDTO {
    private Integer attachmentNo;
    private String originalFileName;
    private String savedFileName;
    private String storeUuid;
    @Builder
    private AttachmentDTO(Integer attachmentNo, String originalFileName,
                          String savedFileName, String storeUuid) {
        this.attachmentNo = attachmentNo;
        this.originalFileName = originalFileName;
        this.savedFileName = savedFileName;
        this.storeUuid = storeUuid;
    }

    public StoreAttachment toEntity() {
        return StoreAttachment.builder()
                .attachmentNo(attachmentNo)
                .originalFileName(originalFileName)
                .savedFileName(savedFileName)
                .storeUuid(storeUuid)
                .build();
    }
}