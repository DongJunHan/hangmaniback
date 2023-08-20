package com.project.hangmani.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PropertiesValues {
    @Value(value="${spring.datasource.driver-class-name}")
    private String DBName;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.upload.domain}")
    private String domain;
}
