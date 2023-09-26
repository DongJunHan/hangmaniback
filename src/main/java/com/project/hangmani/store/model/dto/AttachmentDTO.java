package com.project.hangmani.store.model.dto;

import com.project.hangmani.store.model.entity.StoreAttachment;
import lombok.*;

@Getter
@ToString
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDTO {
    private Integer attachmentNo;
    private String originalFileName;
    private String savedFileName;
    private String storeUuid;
    public StoreAttachment toEntity() {
        return StoreAttachment.builder()
                .attachmentNo(attachmentNo)
                .originalFileName(originalFileName)
                .savedFileName(savedFileName)
                .storeUuid(storeUuid)
                .build();
    }
}