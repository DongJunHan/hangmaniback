package com.project.hangmani.repository;

import com.project.hangmani.config.DatabaseInit;
import com.project.hangmani.domain.StoreAttachment;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class FileRepositoryTest {
    FileRepository fileRepository;
    private DataSource dataSource;
    private JdbcTemplate template;
    private Util util;
    @BeforeEach
    void TestConfig() {
        DatabaseInit dbInit = new DatabaseInit();
        dataSource = dbInit.loadDataSource("jdbc:h2:mem:test;MODE=MySQL;DATABASE_TO_LOWER=TRUE", "sa", "");
        template = dbInit.loadJdbcTemplate(dataSource);
        dbInit.loadScript(template);

        util = new Util();//Mockito.mock(Util.class);
        fileRepository = new FileRepository(dataSource, util);
    }
    @Test
    void insertAttachment() {

    }
    @Test
    void getAttachmentsByUuid() {
        String storeUuid = "8c33e236-ac2c-11ed-9b15-12ebd169e012";
        List<StoreAttachment> attachmentsByUuid = fileRepository.getAttachmentsByUuid(storeUuid);
        for (StoreAttachment st:attachmentsByUuid
             ) {
            Assertions.assertThat(storeUuid).isEqualTo(st.getStoreUuid());
        }

    }
}