package com.project.hangmani.report.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestNoDTO {
    private Integer reportID;
    @Builder
    private RequestNoDTO(Integer reportID) {
        this.reportID = reportID;
    }
}
