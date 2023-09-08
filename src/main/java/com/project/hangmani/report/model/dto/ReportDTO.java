package com.project.hangmani.report.model.dto;

import lombok.*;

import java.sql.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReportDTO {
    private Integer reportNo;
    private Date reportDate;
    private String reportContent;
    private String id;
    private Integer reportID;
    private String reportType;

    public ResponseDTO convertToResponse() {
        return ResponseDTO.builder()
                .reportDate(this.getReportDate())
                .reportContent(this.getReportContent())
                .reportType(this.getReportType())
                .reportNo(this.getReportNo())
                .id(this.getId())
                .build();
    }
}
