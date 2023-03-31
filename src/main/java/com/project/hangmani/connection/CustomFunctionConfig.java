package com.project.hangmani.connection;

import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
public class CustomFunctionConfig {
    private final JdbcTemplate jdbcTemplate;


    public CustomFunctionConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void registerFunctions() {
        String packageName = Util.class.getPackage().getName();
        String className = Util.class.getSimpleName();
        String qualifiedClassName = packageName + "." + className;
        log.info("class={}", qualifiedClassName);

        this.jdbcTemplate.execute("CREATE ALIAS  IF NOT EXISTS distance  FOR \""+qualifiedClassName+".getDistance\";");
    }

}
