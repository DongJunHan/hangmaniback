package com.project.hangmani.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DatabaseInit {
    public void loadScript(JdbcTemplate template) {
        Resource drop = new ClassPathResource("drop.sql");
        Resource schema = new ClassPathResource("schema.sql");
        Resource data = new ClassPathResource("data.sql");
        try {
            ScriptUtils.executeSqlScript(template.getDataSource().getConnection(), drop);
            ScriptUtils.executeSqlScript(template.getDataSource().getConnection(), schema);
            ScriptUtils.executeSqlScript(template.getDataSource().getConnection(), data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DataSource loadDataSource() {
        return new DriverManagerDataSource(
                "jdbc:h2:mem:test;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
                "sa", "");
    }

    public JdbcTemplate loadJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
