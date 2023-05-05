package com.project.hangmani.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Date;

public class ReportDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestReportDTO {
        @NotBlank
        private String id;
        private String reportContent;
        private int reportID;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseReportDTO {
        private int reportNo;
        private String reportContent;
        private String reportType;
        private String id;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseReportDetailDTO {
        private int reportNo;
        private Date reportDate;
        private String reportContent;
        private String reportType;
        private String id;
    }
}
