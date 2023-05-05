package com.project.hangmani.service;

import com.project.hangmani.domain.Report;
import com.project.hangmani.dto.ReportDTO.RequestReportDTO;
import com.project.hangmani.dto.ReportDTO.ResponseReportDTO;
import com.project.hangmani.dto.ReportDTO.ResponseReportDetailDTO;
import com.project.hangmani.exception.NotFoundReport;
import com.project.hangmani.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    private ReportRepository reportRepository;
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
    @Transactional
    public ResponseReportDTO reportClosure(RequestReportDTO reportDTO) {
        int reportNo = reportRepository.reportClosure(reportDTO);
        List<Report> reports = reportRepository.reportByNo(reportNo);
        if (reports.size() == 0)
            throw new NotFoundReport();
        Report report = reports.get(0);
        return ResponseReportDTO.builder()
                .id(report.getId())
                .reportType(report.getReportType())
                .reportContent(report.getReportContent())
                .reportNo(report.getReportNo())
                .build();
    }

    public ResponseReportDetailDTO reportByNo(int reportNo){
        List<Report> reports = reportRepository.reportByNo(reportNo);
        if (reports.size() == 0)
            throw new NotFoundReport();
        Report report = reports.get(0);
        return ResponseReportDetailDTO.builder()
                .reportDate(report.getReportDate())
                .reportContent(report.getReportContent())
                .reportType(report.getReportType())
                .reportNo(report.getReportNo())
                .id(report.getId())
                .build();
    }

    public List<ResponseReportDetailDTO> reportByUserId(String userId) {
        List<Report> reports = reportRepository.reportByUserId(userId);
        if (reports.size() == 0)
            throw new NotFoundReport();
        List<ResponseReportDetailDTO> ret = new ArrayList<>();
        for (Report report:
             reports) {
            ret.add(ResponseReportDetailDTO.builder()
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
