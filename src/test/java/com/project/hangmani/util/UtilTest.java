package com.project.hangmani.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class UtilTest {
    private Util util = new Util();


    @Test
    void savedAttachmentFile() {
        Path path = Paths.get("src/test/resources/attachment/HTTP.png");
        byte[] bytes = null;
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