package com.project.hangmani.model.management;

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
    public BoardRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public Board insertBoard(Board board) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(template);
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("boardwriter", board.getBoardWriter())
                .addValue("content", board.getContent())
                .addValue("title", board.getTitle())
                .addValue("createat", getDate())
                .addValue("updateat", getDate());

        KeyHolder key = jdbcInsert
                .withTableName("BOARD")
                .usingGeneratedKeyColumns("BOARDNO")
//                .usingColumns("boardwriter","content","title", "createat","updateat")
//                .withoutTableColumnMetaDataAccess()
                .executeAndReturnKeyHolder(params);

        board.setBoardNo(key.getKey().intValue());
        return board;
    }
    private Date getDate() {
        java.util.Date date = new java.util.Date();
        long time = date.getTime();
        return new Date(time);
    }
}
