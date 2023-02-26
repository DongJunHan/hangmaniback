package com.project.hangmani.service;

import com.project.hangmani.model.winhistory.WinHistory;
import com.project.hangmani.model.winhistory.WinHistoryRepository;
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
        return winHistoryRepository.findByArea(sido, sigugun);
    }
}
