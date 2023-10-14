package com.project.hangmani.report.model.dto;

import com.project.hangmani.report.model.entity.Report;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestInsertDTO {
    @NotBlank
    private String id;
    private String reportContent;
    private Integer reportID;
    @Builder
    private RequestInsertDTO(String id, String reportContent, Integer reportID) {
        this.id = id;
        this.reportContent = reportContent;
        this.reportID = reportID;
    }

    public Report convertToEntity(RequestInsertDTO this) {
        return Report.builder()
                .id(this.id)
                .reportContent(this.reportContent)
                .reportID(this.reportID)
                .build();
    }
}
