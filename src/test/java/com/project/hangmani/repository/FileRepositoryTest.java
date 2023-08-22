package com.project.hangmani.repository;

import com.project.hangmani.config.DatabaseInit;
import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.domain.StoreAttachment;
import com.project.hangmani.security.AES;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties"
})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        initializers = {ConfigDataApplicationContextInitializer.class},
        classes = {AES.class, PropertiesValues.class}
)
class FileRepositoryTest {
    FileRepository fileRepository;
    private static DataSource dataSource;
    private static JdbcTemplate template;
    private static DatabaseInit dbInit;
    @Autowired
    private PropertiesValues propertiesValues;
    @BeforeAll
    static void initOnce() {
        dbInit = new DatabaseInit();
        dataSource = dbInit.loadDataSource();
        template = dbInit.loadJdbcTemplate(dataSource);
    }
    @BeforeEach
    void TestConfig() {
        dbInit.loadScript(template);
        fileRepository = new FileRepository(dataSource, propertiesValues);
    }

    @Test
    void insertAttachment() {

    }

    @Test
    void getAttachmentsByUuid() {
        String storeUuid = "8c33e236-ac2c-11ed-9b15-12ebd169e012";
        List<StoreAttachment> attachmentsByUuid = fileRepository.getAttachmentsByUuid(storeUuid);
        for (StoreAttachment st : attachmentsByUuid
        ) {
            assertThat(storeUuid).isEqualTo(st.getStoreUuid());
        }

    }
}