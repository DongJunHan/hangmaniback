package com.project.hangmani.controller;

import com.project.hangmani.dto.ReportDTO;
import com.project.hangmani.dto.ReportDTO.RequestReportDTO;
import com.project.hangmani.dto.ReportDTO.ResponseReportDTO;
import com.project.hangmani.dto.ReportDTO.ResponseReportDetailDTO;
import com.project.hangmani.dto.ResponseDTO;
import com.project.hangmani.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {
    private ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseDTO<ResponseReportDTO> reportClosure(@ModelAttribute @Valid RequestReportDTO reportDTO) {
        return ResponseDTO.<ResponseReportDTO>builder()
                .data(reportService.reportClosure(reportDTO))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }
    @GetMapping({"userId"})
    public ResponseDTO<List<ResponseReportDetailDTO>> reportByUserId(@PathVariable String userId) {
        return ResponseDTO.<List<ResponseReportDetailDTO>>builder()
                .data(reportService.reportByUserId(userId))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }
    @GetMapping("{reportNo}")
    public ResponseDTO<ResponseReportDetailDTO> reportDetail(@PathVariable int reportNo){
        return ResponseDTO.<ResponseReportDetailDTO>builder()
                .data(reportService.reportByNo(reportNo))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }
}
