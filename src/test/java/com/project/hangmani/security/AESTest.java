package com.project.hangmani.security;

import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class AESTest {
    AES AES;
    Util util;
    @BeforeEach
    void beforeEach() {
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(AES.class);
        ConfigurableApplicationContext utilac = new AnnotationConfigApplicationContext(Util.class);
        AES = ac.getBean(AES.class);
        util = utilac.getBean(Util.class);
    }
    @AfterEach
    void afterEach() {
        log.info("end!");
//        File file = new File(KEY_FILE_PATH);
//        if (file.exists()){
//            file.delete();
//            log.info("file delete={}", file.exists());
//        }
    }
    @Test
    void encryptData() {
        byte[] testTexts = AES.encryptData("testText", StandardCharsets.UTF_8);
        ConvertData convertData = new ConvertData();
        String base64 = convertData.byteToBase64(testTexts);

        log.info("base64={}", base64);
        log.info("encrypt={}", testTexts);
        assertThat(base64).isEqualTo("iN4DKYNOzYNc4l4Jm3xJwg==");
    }

    @Test
    void decryptData() {
        String base64 = "iN4DKYNOzYNc4l4Jm3xJwg==";
        ConvertData convertData = new ConvertData();
        byte[] bytes = convertData.base64ToByte(base64);
        String ret = convertData.byteToString(AES.decryptData(bytes), StandardCharsets.UTF_8);
        assertThat(ret).isEqualTo("testText");


    }
}