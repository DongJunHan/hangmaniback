package com.project.hangmani.security;

import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class AESTest {
    AES AES;
    Util util;

    @BeforeEach
    void beforeEach() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.register(AES.class);
        ac.register(Util.class);
        ac.refresh();
        AES = ac.getBean(AES.class);
        util = ac.getBean(Util.class);
    }
    @AfterEach
    void afterEach() {
//        File file = new File(KEY_FILE_PATH);
//        if (file.exists()){
//            file.delete();
//            log.info("file delete={}", file.exists());
//        }
    }
    @Test
    @DisplayName("Decrypt to Encrypt")
    void encryptData(TestReporter testReporter) {
        byte[] testTexts = AES.encryptData("testText", StandardCharsets.UTF_8);
        ConvertData convertData = new ConvertData();
        String base64 = convertData.byteToBase64(testTexts);
        testReporter.publishEntry("[encryptData] base64", base64);
        System.out.println("[encryptData] base64 = " + base64);
        assertThat(base64).isEqualTo("iN4DKYNOzYNc4l4Jm3xJwg==");
    }

    @Test
    @DisplayName("Encrypt to Decrypt")
    void decryptData(TestReporter testReporter) {
        String base64 = "iN4DKYNOzYNc4l4Jm3xJwg==";
        testReporter.publishEntry("[decryptData] base64", base64);
        System.out.println("[decryptData] base64 = " + base64);
        ConvertData convertData = new ConvertData();
        byte[] bytes = convertData.base64ToByte(base64);
        String ret = convertData.byteToString(AES.decryptData(bytes), StandardCharsets.UTF_8);
        testReporter.publishEntry("[decryptData] plainText", ret);
        System.out.println("[decryptData] plainText = " + ret);
        assertThat(ret).isEqualTo("testText");
    }
}