package com.project.hangmani.report.model.entity;

import lombok.*;

import java.sql.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Report {
    private int reportNo;
    private Date reportDate;
    private String reportContent;
    private String id;
    private int reportID;
    private String reportType;
    @Builder
    private Report(int reportNo, Date reportDate, String reportContent,
                   String id, int reportID, String reportType) {
        this.reportNo = reportNo;
        this.reportDate = reportDate;
        this.reportContent = reportContent;
        this.id = id;
        this.reportID = reportID;
        this.reportType = reportType;
    }
}
