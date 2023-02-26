package com.project.hangmani.model.winhistory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class WinHistoryRepository {
    private final JdbcTemplate template;

    public WinHistoryRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

//    public List<WinHistory> findByArea(String sido, String sigugun) {
//        String sql = "SELECT win_history.storeuuid,lottoid,winround,winrank FROM Store INNER JOIN win_history on store.storeuuid = win_history.storeuuid where store.storesido= ? and store.storesigugun= ?;";
//        SqlRowSet sqlRowSet = template.queryForLis(sql, sido, sigugun);
//        while (sqlRowSet.next()){
//
//            sqlRowSet.getObject(WinHistory);
//        }
//    }
    private RowMapper<WinHistory> winHistoryRowMapper(){
        return (rs, rowNum) -> {
            WinHistory winHistory = new WinHistory();
            winHistory.setWinRank(rs.getInt("winrank"));
            winHistory.setStoreUuid(rs.getString("storeuuid"));
            winHistory.setWinRound(rs.getInt("winround"));
            winHistory.setLottoId(rs.getInt("lottoid"));
            return winHistory;
        };
    }
}
