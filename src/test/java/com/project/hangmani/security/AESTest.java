package com.project.hangmani.security;

import com.project.hangmani.util.ConvertData;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = {
        "file:../hangmani_config/application-local.properties",
        "classpath:application.properties"
})
@SpringBootTest
class AESTest {
    @Autowired
    private AES aes;
    @Autowired
    private ConvertData convertData;

    @Test
    @DisplayName("Decrypt to Encrypt")
    void encryptData(TestReporter testReporter) {
        byte[] testTexts = aes.encryptData("testText", StandardCharsets.UTF_8);
        String base64 = convertData.byteToBase64(testTexts);
        testReporter.publishEntry("[encryptData] base64", base64);
        assertThat(base64).isEqualTo("iN4DKYNOzYNc4l4Jm3xJwg==");
    }

    @Test
    @DisplayName("Encrypt to Decrypt")
    void decryptData(TestReporter testReporter) {
        String base64 = "iN4DKYNOzYNc4l4Jm3xJwg==";
        testReporter.publishEntry("[decryptData] base64", base64);
        byte[] bytes = convertData.base64ToByte(base64);
        String ret = convertData.byteToString(aes.decryptData(bytes), StandardCharsets.UTF_8);
        testReporter.publishEntry("[decryptData] plainText", ret);
        assertThat(ret).isEqualTo("testText");
    }
}