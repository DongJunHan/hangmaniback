package com.project.hangmani.winHistory.repository;

import com.project.hangmani.winHistory.model.entity.WinHistory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class WinHistoryRepository {
    private final JdbcTemplate template;
    private final String findByAreaSql =
            "SELECT win_history.storeuuid,lottoid,winround,winrank FROM Store INNER JOIN win_history " +
                    "on store.storeuuid = win_history.storeuuid where store.storesido= ? and store.storesigugun= ?;";

    public WinHistoryRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public List<WinHistory> findWinHistoryByArea(String sido, String sigugun) {
        return template.query(findByAreaSql,new Object[]{sido, sigugun}, winHistoryRowMapper());
    }
    private RowMapper<WinHistory> winHistoryRowMapper(){
        return (rs, rowNum) -> {
            return WinHistory.builder()
                    .storeUuid(rs.getString("storeuuid"))
                    .winRank(rs.getInt("winRank"))
                    .winRound(rs.getInt("winRound"))
                    .lottoId(rs.getInt("lottoId"))
                    .build();
        };
    }
}
