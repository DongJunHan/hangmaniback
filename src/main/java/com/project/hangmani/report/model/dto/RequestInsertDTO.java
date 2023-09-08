package com.project.hangmani.report.model.dto;

import com.project.hangmani.report.model.entity.Report;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestInsertDTO {
    @NotBlank
    private String id;
    private String reportContent;
    private Integer reportID;

    public Report convertToEntity(RequestInsertDTO this) {
        return Report.builder()
                .id(this.id)
                .reportContent(this.reportContent)
                .reportID(this.reportID)
                .build();
    }
}
