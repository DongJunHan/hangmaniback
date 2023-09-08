package com.project.hangmani.report.service;

import com.project.hangmani.exception.NotFoundReport;
import com.project.hangmani.report.model.dto.ReportDTO;
import com.project.hangmani.report.model.dto.RequestInsertDTO;
import com.project.hangmani.report.model.dto.ResponseDTO;
import com.project.hangmani.report.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportService {
    private ReportRepository reportRepository;
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
    @Transactional
    public ResponseDTO reportClosure(RequestInsertDTO reportDTO) {
        int reportNo = reportRepository.add(reportDTO.convertToEntity());
        ReportDTO report = checkByNo(reportNo);
        return ResponseDTO.builder()
                .id(report.getId())
                .reportType(report.getReportType())
                .reportContent(report.getReportContent())
                .reportNo(report.getReportNo())
                .build();
    }

    public ResponseDTO reportByNo(int reportNo){
        ReportDTO report = checkByNo(reportNo);

        return report.convertToResponse();
    }

    public List<ResponseDTO> reportByUserId(String userID) {
        List<ReportDTO> reports = checkByID(userID);
        return new ResponseDTO().convertToDTOs(reports);
    }

    private ReportDTO checkByNo(int reportNo) {
        List<ReportDTO> reports = reportRepository.getByNo(reportNo);
        if (reports.size() == 0)
            throw new NotFoundReport();
        return reports.get(0);
    }

    private List<ReportDTO> checkByID(String userID) {
        List<ReportDTO> reports = reportRepository.getByUserId(userID);
        if (reports.size() == 0)
            throw new NotFoundReport();
        return reports;
    }
}
