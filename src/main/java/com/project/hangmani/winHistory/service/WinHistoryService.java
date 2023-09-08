package com.project.hangmani.winHistory.service;

import com.project.hangmani.winHistory.model.entity.WinHistory;
import com.project.hangmani.winHistory.repository.WinHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service
public class WinHistoryService {
    private final WinHistoryRepository winHistoryRepository;

    public WinHistoryService(WinHistoryRepository winHistoryRepository) {
        this.winHistoryRepository = winHistoryRepository;
    }

    public List<WinHistory> getData(String sido, String sigugun){
        return winHistoryRepository.findWinHistoryByArea(sido, sigugun);
    }
}
