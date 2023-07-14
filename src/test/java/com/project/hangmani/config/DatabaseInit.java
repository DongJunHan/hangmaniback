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

    public DataSource loadDataSource(String url, String user, String password) {
        return new DriverManagerDataSource(url,user,password);
    }

    public JdbcTemplate loadJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
