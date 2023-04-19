package com.project.hangmani.repository;

import com.project.hangmani.domain.StoreAttachment;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
class FileRepositoryTest {
    @Autowired
    FileRepository fileRepository;
    @Test
    void insertAttachment() {

    }
    @Test
    void getAttachmentsByUuid() {
        String storeUuid = "8c33e236-ac2c-11ed-9b15-12ebd169e012";
        List<StoreAttachment> attachmentsByUuid = fileRepository.getAttachmentsByUuid(storeUuid);
        log.info("attach={}", attachmentsByUuid);
        for (StoreAttachment st:attachmentsByUuid
             ) {
            Assertions.assertThat(storeUuid).isEqualTo(st.getStoreUuid());
        }

    }
}