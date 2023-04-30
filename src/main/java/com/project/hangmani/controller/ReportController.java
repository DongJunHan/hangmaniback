package com.project.hangmani.controller;

import com.project.hangmani.dto.ResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {
    @PostMapping
    public ResponseDTO<Object> reportClosure() {
        return null;
    }
}
