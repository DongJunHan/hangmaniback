package com.project.hangmani.controller;

import com.project.hangmani.common.dto.Response;
import com.project.hangmani.report.model.dto.RequestInsertDTO;
import com.project.hangmani.report.model.dto.ResponseDTO;
import com.project.hangmani.report.service.ReportService;
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
    public Response<ResponseDTO> reportClosure(@ModelAttribute @Valid RequestInsertDTO reportDTO) {
        return Response.<ResponseDTO>builder()
                .data(reportService.reportClosure(reportDTO))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }
    @GetMapping({"userId"})
    public Response<List<ResponseDTO>> reportByUserId(@PathVariable String userId) {
        return Response.<List<ResponseDTO>>builder()
                .data(reportService.reportByUserId(userId))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }
    @GetMapping("{reportNo}")
    public Response<ResponseDTO> reportDetail(@PathVariable int reportNo){
        return Response.<ResponseDTO>builder()
                .data(reportService.reportByNo(reportNo))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }
}
