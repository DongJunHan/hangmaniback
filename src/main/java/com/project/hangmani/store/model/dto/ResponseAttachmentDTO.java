package com.project.hangmani.store.model.dto;

import lombok.*;

import java.util.List;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseAttachmentDTO {
    private List<String> domainUrls;
    private String storeUuid;
    @Builder
    private ResponseAttachmentDTO(List<String> domainUrls, String storeUuid) {
        this.domainUrls = domainUrls;
        this.storeUuid = storeUuid;
    }
}
