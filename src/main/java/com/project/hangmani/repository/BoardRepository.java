package com.project.hangmani.repository;

import com.project.hangmani.domain.Board;
import com.project.hangmani.util.ConvertData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class BoardRepository {
    private final String findByIdSql = "select * from board where boardno=?";
    private final String deleteByNo = "update board set isdelete = 1 where boardno=?";
    private JdbcTemplate template;
    private ConvertData convertData;
    public BoardRepository(DataSource dataSource) {
        this.convertData = new ConvertData();
        this.template = new JdbcTemplate(dataSource);
    }

    public Optional<Board> findByNo(int no) {
        List<Board> list = template.query(findByIdSql, boardRowMapper(), no);
        return list.stream().findAny();
    }
    public int deleteByNo(int boardNo) {
        return template.update(deleteByNo,boardNo);
    }
    public int insertBoard(Board board) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(template);
        Date createAt = convertData.getSqlDate();
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("boardwriter", board.getBoardWriter())
                .addValue("boardcontent", board.getBoardContent())
                .addValue("boardtitle", board.getBoardTitle())
                .addValue("createat", createAt)
                .addValue("updateat", createAt);

        KeyHolder key = jdbcInsert
                .withTableName("BOARD")
                .usingGeneratedKeyColumns("BOARDNO")
//                .usingColumns("boardwriter","content","title", "createat","updateat")
//                .withoutTableColumnMetaDataAccess()
                .executeAndReturnKeyHolder(params);
        return key.getKey().intValue();
    }
    private RowMapper<Board> boardRowMapper() {
        return (rs, rowNum) -> {
            Board board = new Board();
            board.setBoardNo(rs.getInt("boardno"));
            board.setBoardContent(rs.getString("boardcontent"));
            board.setBoardTitle(rs.getString("boardtitle"));
            board.setUpdateAt(rs.getDate("updateat"));
            board.setCreateAt(rs.getDate("createat"));
            board.setBoardWriter(rs.getString("boardwriter"));
            board.setDelete(rs.getBoolean("isdelete"));
            return board;
        };
    }
}
