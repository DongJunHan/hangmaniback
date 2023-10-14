package com.project.hangmani.report.model.dto;

import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ResponseDTO {
    private Integer reportNo;
    private String reportContent;
    private String reportType;
    private String id;
    private Date reportDate;
    @Builder
    private ResponseDTO(Integer reportNo, String reportContent,
                        String reportType, String id, Date reportDate) {
        this.reportNo = reportNo;
        this.reportContent = reportContent;
        this.reportType = reportType;
        this.id = id;
        this.reportDate = reportDate;
    }

    public List<ResponseDTO> convertToDTOs(List<ReportDTO> reports) {
        List<ResponseDTO> ret = new ArrayList<>();
        for (ReportDTO report:
                reports) {
            ret.add(ResponseDTO.builder()
                    .id(report.getId())
                    .reportDate(report.getReportDate())
                    .reportContent(report.getReportContent())
                    .reportType(report.getReportType())
                    .reportNo(report.getReportNo())
                    .build());
        }
        return ret;
    }

}
