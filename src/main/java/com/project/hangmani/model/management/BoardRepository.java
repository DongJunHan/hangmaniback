package com.project.hangmani.model.management;

import com.project.hangmani.util.ConvertData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.HashMap;
import java.util.Objects;

@Repository
@Slf4j
public class BoardRepository {
    private JdbcTemplate template;
    private ConvertData convertData;
    public BoardRepository(DataSource dataSource) {
        this.convertData = new ConvertData();
        this.template = new JdbcTemplate(dataSource);
    }

    public Board insertBoard(Board board) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(template);
        Date createAt = convertData.getSqlDate();
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("boardwriter", board.getBoardWriter())
                .addValue("content", board.getContent())
                .addValue("title", board.getTitle())
                .addValue("createat", createAt)
                .addValue("updateat", createAt);

        KeyHolder key = jdbcInsert
                .withTableName("BOARD")
                .usingGeneratedKeyColumns("BOARDNO")
//                .usingColumns("boardwriter","content","title", "createat","updateat")
//                .withoutTableColumnMetaDataAccess()
                .executeAndReturnKeyHolder(params);
        board.setCreateAt(createAt);
        board.setUpdateAt(createAt);
        board.setBoardNo(key.getKey().intValue());
        return board;
    }

}
