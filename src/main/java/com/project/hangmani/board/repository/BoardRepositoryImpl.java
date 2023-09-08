package com.project.hangmani.board.repository;

import com.project.hangmani.board.model.dto.BoardDTO;
import com.project.hangmani.board.model.dto.RequestNoDTO;
import com.project.hangmani.board.model.dto.RequestRangeDTO;
import com.project.hangmani.board.model.dto.RequestWriterDTO;
import com.project.hangmani.board.model.entity.Board;
import com.project.hangmani.config.CustomFunctionConfig;
import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.util.Util;
import lombok.RequiredArgsConstructor;
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
public class BoardRepositoryImpl implements BoardRepository{
    private final String BY_BOARDNO = " WHERE boardno=?";
    private final String BY_WRITER = " WHERE boardWriter=?";
    private final String SELECT_QUERY = "SELECT * FROM BOARD";

    private final String LIMIT = " LIMIT ?,?";
    private final String deleteByNo = "update board set isdelete = 1 where boardno=?";
    private final JdbcTemplate template;
    private Util util;
    public BoardRepositoryImpl(DataSource dataSource, PropertiesValues propertiesValues) {
        this.util = new Util(propertiesValues);
        this.template = new JdbcTemplate(dataSource);
    }

    public Optional<BoardDTO> getByNo(int boardNo, int offset, int limit) {
        List<BoardDTO> list = template.query(SELECT_QUERY+BY_BOARDNO+LIMIT,new Object[]{boardNo, offset, limit}, boardRowMapper());
        return list.stream().findAny();
    }

    public List<BoardDTO> getList(RequestRangeDTO boardDTO) {
        return template.query(SELECT_QUERY + LIMIT, new Object[]{boardDTO.getOffset(), boardDTO.getLimit()},boardRowMapper());
    }
    public List<BoardDTO> getByWriter(RequestWriterDTO boardDTO) {
        return template.query(SELECT_QUERY + BY_WRITER + LIMIT,
                new Object[]{boardDTO.getBoardWriter(), boardDTO.getOffset(), boardDTO.getLimit()}, boardRowMapper());
    }
    public int delete(int boardNo) {
        return template.update(deleteByNo,boardNo);
    }
    public int add(Board board) {
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
    private RowMapper<BoardDTO> boardRowMapper() {
        return (rs, rowNum) -> {
            BoardDTO board = new BoardDTO();
            board.setBoardNo(rs.getInt("boardNo"));
            board.setBoardContent(rs.getString("content"));
            board.setBoardTitle(rs.getString("title"));
            board.setUpdateAt(rs.getDate("updateAt"));
            board.setCreateAt(rs.getDate("createAt"));
            board.setBoardWriter(rs.getString("boardWriter"));
            board.setIsDelete(rs.getBoolean("isdelete"));
            return board;
        };
    }
}
