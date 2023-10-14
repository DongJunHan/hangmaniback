package com.project.hangmani.report.model.dto;

import lombok.*;

import java.sql.Date;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ReportDTO {
    private Integer reportNo;
    private Date reportDate;
    private String reportContent;
    private String id;
    private Integer reportID;
    private String reportType;
    @Builder
    private ReportDTO(Integer reportNo, Date reportDate, String reportContent,
                      String id, Integer reportID, String reportType) {
        this.reportNo = reportNo;
        this.reportDate = reportDate;
        this.reportContent = reportContent;
        this.id = id;
        this.reportID = reportID;
        this.reportType = reportType;
    }

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
