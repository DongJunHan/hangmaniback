package com.project.hangmani.store.model.entity;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreAttachment {
    private int attachmentNo;
    private String originalFileName;
    private String savedFileName;
    private String storeUuid;
    @Builder
    private StoreAttachment(int attachmentNo, String originalFileName, String savedFileName, String storeUuid) {
        this.attachmentNo = attachmentNo;
        this.originalFileName = originalFileName;
        this.savedFileName = savedFileName;
        this.storeUuid = storeUuid;
    }
}
