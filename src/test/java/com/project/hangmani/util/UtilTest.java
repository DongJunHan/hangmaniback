package com.project.hangmani.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
class UtilTest {
    @Autowired
    private Util util;
    @Test
    void savedAttachmentFile() {
        Path path = Paths.get("/opt/uploads/store/IMG_0339 copy.jpg");
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InputStream is = new ByteArrayInputStream(bytes);
        this.util.savedAttachmentFile(is, path.getFileName().toString());
    }
}