package com.project.hangmani.repository;

import com.project.hangmani.config.CustomFunctionConfig;
import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.domain.Board;
import com.project.hangmani.dto.BoardDTO.RequestBoardDTO;
import com.project.hangmani.util.Util;
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
    private final String BY_BOARDNO = " WHERE boardno=?";
    private final String BY_WRITER = " WHERE boardWriter=?";
    private final String SELECT_QUERY = "SELECT * FROM BOARD";

    private final String LIMIT = " LIMIT ?,?";
    private final String deleteByNo = "update board set isdelete = 1 where boardno=?";
    private JdbcTemplate template;
    private Util util;
    private CustomFunctionConfig customFunctionConfig;
    public BoardRepository(DataSource dataSource, PropertiesValues propertiesValues) {
        this.util = new Util(propertiesValues);
        this.template = new JdbcTemplate(dataSource);
        this.customFunctionConfig = new CustomFunctionConfig(this.template, propertiesValues);
    }

    public Optional<Board> findByNo(int no, int offset, int limit) {
        List<Board> list = template.query(SELECT_QUERY+BY_BOARDNO+LIMIT,new Object[]{no, offset, limit}, boardRowMapper());
        return list.stream().findAny();
    }

    public List<Board> getBoards(RequestBoardDTO boardDTO) {
        return template.query(SELECT_QUERY + LIMIT, new Object[]{boardDTO.getOffset(), boardDTO.getLimit()},boardRowMapper());
    }
    public List<Board> getBoardsByWriter(RequestBoardDTO boardDTO) {
        return template.query(SELECT_QUERY + BY_WRITER + LIMIT,
                new Object[]{boardDTO.getBoardWriter(), boardDTO.getOffset(), boardDTO.getLimit()}, boardRowMapper());
    }
    public int deleteByNo(int boardNo) {
        return template.update(deleteByNo,boardNo);
    }
    public int insertBoard(Board board) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(template);
        Date createAt = util.getSqlDate();
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("boardWriter", board.getBoardWriter())
                .addValue("content", board.getBoardContent())
                .addValue("title", board.getBoardTitle())
                .addValue("createAt", createAt)
                .addValue("updateAt", createAt);

        KeyHolder key = jdbcInsert
                .withTableName("BOARD")
                .usingGeneratedKeyColumns("BOARDNO")
                .usingColumns("boardWriter","content","title", "createAt","updateAt")
                .withoutTableColumnMetaDataAccess()
                .executeAndReturnKeyHolder(params);
        return key.getKey().intValue();
    }
    private RowMapper<Board> boardRowMapper() {
        return (rs, rowNum) -> {
            Board board = new Board();
            board.setBoardNo(rs.getInt("boardNo"));
            board.setBoardContent(rs.getString("content"));
            board.setBoardTitle(rs.getString("title"));
            board.setUpdateAt(rs.getDate("updateAt"));
            board.setCreateAt(rs.getDate("createAt"));
            board.setBoardWriter(rs.getString("boardWriter"));
            board.setDelete(rs.getBoolean("isdelete"));
            return board;
        };
    }
}
