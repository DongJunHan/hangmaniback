package com.project.hangmani.controller;

import com.project.hangmani.service.WinHistoryService;
import com.project.hangmani.util.ConvertData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/winhistory")
@Slf4j
public class WinHistoryController {
    private final WinHistoryService winHistoryService;
    private final ConvertData convertData;

    public WinHistoryController(WinHistoryService winHistoryService) {
        this.convertData = new ConvertData();
        this.winHistoryService = winHistoryService;
    }


}
