package com.project.hangmani.model.management;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class BoardRepository {
    private JdbcTemplate template;
    public BoardRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }
}
