package com.project.hangmani.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAttachmentDTO {
    private List<String> domainUrls;
    private String storeUuid;
}
