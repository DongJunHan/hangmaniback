package com.project.hangmani.controller;

import com.project.hangmani.model.winhistory.WinHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/winhistory")
@Slf4j
public class WinHistoryController {
    private final WinHistoryRepository winHistoryRepository;

    public WinHistoryController(WinHistoryRepository winHistoryRepository) {
        this.winHistoryRepository = winHistoryRepository;
    }

}
