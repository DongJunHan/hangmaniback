package com.project.hangmani.repository;

import com.project.hangmani.file.repository.FileRepository;
import com.project.hangmani.store.model.dto.AttachmentDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties",
        "classpath:/application-test.properties"
})
@SpringBootTest
@Sql(value = {"classpath:drop.sql", "classpath:schema.sql", "classpath:data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FileRepositoryTest {
    @Autowired
    private FileRepository fileRepository;
    @Test
    void insertAttachment() {

    }

    @Test
    void getAttachmentsByUuid() {
        String storeUuid = "8c33e236-ac2c-11ed-9b15-12ebd169e012";
        List<AttachmentDTO> attachmentsByUuid = fileRepository.getAttachmentsByUuid(storeUuid);
        for (AttachmentDTO st : attachmentsByUuid
        ) {
            assertThat(storeUuid).isEqualTo(st.getStoreUuid());
        }

    }
}