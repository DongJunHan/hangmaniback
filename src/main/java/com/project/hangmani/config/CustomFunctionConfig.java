package com.project.hangmani.config;

import com.project.hangmani.util.Util;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
public class CustomFunctionConfig {
    private final JdbcTemplate jdbcTemplate;
    private String dbName;


    public CustomFunctionConfig(JdbcTemplate jdbcTemplate, PropertiesValues propertiesValues) {
        this.dbName = propertiesValues.getDBName();
        this.jdbcTemplate = jdbcTemplate;
    }
    @PostConstruct
    public void registerFunctions() {
        if (null != dbName && dbName.contains("h2")) {
            String packageName = Util.class.getPackage().getName();
            String className = Util.class.getSimpleName();
            String qualifiedClassName = packageName + "." + className;
//        Delete alias
            this.jdbcTemplate.execute("DROP ALIAS IF EXISTS get_distance;");
            this.jdbcTemplate.execute("CREATE ALIAS IF NOT EXISTS get_distance  FOR \"" + qualifiedClassName + ".getDistance\";");
        }
    }

}
