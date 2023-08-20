package com.project.hangmani.util;

import com.project.hangmani.config.PropertiesValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Import({PropertiesValues.class, Util.class})
@TestPropertySource(locations = {
        "file:/Users/handongjun/workspace/team_project/hangmani_config/application-local.properties",
        "classpath:application.properties"
})
class UtilTest {
    private Util util;
    @Autowired
    private PropertiesValues propertiesValues;
    @BeforeEach
    void UtilCLassInit() {
        util = new Util(propertiesValues);
    }
    @Test
    void savedAttachmentFile() {
        String url = util.generateFileUrl("src/test/resources/attachment/HTTP.pngs");
        System.out.println("@@url = " + url);
        Path path = Paths.get("src/test/resources/attachment/HTTP.png");
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InputStream is = new ByteArrayInputStream(bytes);
        this.util.savedAttachmentFile(is, path.getFileName().toString());
    }

    @Test
    @DisplayName("No such attachment file")
    void noSuchAttachmentFile() {
        Path path = Paths.get("HTTP.png");
        assertThrows(NoSuchFileException.class, () -> Files.readAllBytes(path));
    }
}