package com.project.hangmani.config;

import com.project.hangmani.winHistory.repository.WinHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableAsync
@EnableScheduling
public class ScheduledWinCount {
    private final WinHistoryRepository winHistoryRepository;
    @Scheduled(cron = "0 0 0 * * 1") //매주 월요일 00시 00분에 수행
    public void CountingWinCountByWinHistory() {

    }
}
