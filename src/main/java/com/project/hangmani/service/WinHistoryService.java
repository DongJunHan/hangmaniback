package com.project.hangmani.service;

import com.project.hangmani.model.winhistory.WinHistoryRepository;
import org.springframework.stereotype.Service;

@Service
public class WinHistoryService {
    private final WinHistoryRepository winHistoryRepository;

    public WinHistoryService(WinHistoryRepository winHistoryRepository) {
        this.winHistoryRepository = winHistoryRepository;
    }
}
