package com.project.hangmani.report.repository;

import com.project.hangmani.report.model.dto.ReportDTO;
import com.project.hangmani.report.model.entity.Report;

import java.util.List;

public interface ReportRepository {
    List<ReportDTO> getByNo(int reportNo);
    int add(Report report);
    List<ReportDTO> getByUserId(String userId);
}
