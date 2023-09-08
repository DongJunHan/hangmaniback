package com.project.hangmani.report.model.dto;

import com.project.hangmani.report.model.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseDTO {
    private Integer reportNo;
    private String reportContent;
    private String reportType;
    private String id;
    private Date reportDate;
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
