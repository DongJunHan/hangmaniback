package com.project.hangmani.util;

import com.project.hangmani.exception.IO;
import com.project.hangmani.exception.InvalidKeySpec;
import com.project.hangmani.exception.NoSuchAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.UUID;

import static com.project.hangmani.config.SecurityConst.*;
@Slf4j
public class Util {
    public byte[] getKeyFromFile() {
        Path path = Paths.get(KEY_FILE_PATH);
        File file = path.toFile();
        if (!file.exists())
            return null;
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new IO(e);
        }
    }

    public PublicKey getPublicKeyFromFile() {
        try {
            byte[] bytes = readByteFile(KEY_FILE_PATH);
            if ( bytes == null || bytes.length == 0)
                return null;

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        }catch (IOException e){
            throw new IO(e);
        }catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithm("wrong algorithm", e);
        } catch (InvalidKeySpecException e) {
            throw new InvalidKeySpec("invalid key spec", e);
        }
    }
    private byte[] readByteFile(String keyFilePath) throws IOException {
        Path path = Paths.get(keyFilePath);
        File file = path.toFile();
        if (!file.exists())
            return null;
        return Files.readAllBytes(path);
    }

    public void saveByteData(byte[] data) {
        Path path = Paths.get(KEY_FILE_PATH);
        try {
            Files.write(path, data);
        } catch (IOException e) {
            throw new IO(e);
        }
    }
    public boolean isBase64(String input) {
        //check input data
        if (input.length() % 4 != 0)
            return false;
        // Base64 문자셋에 속하는지 확인
        if (!input.matches("^[A-Za-z0-9+/]*[=]{0,2}$"))
            return false;
        // Base64 문자셋에 속하는지 확인
//        for (char c : input.toCharArray()) {
//            if (!Character.isLetterOrDigit(c) && c != '+' && c != '/') {
//                return false;
//            }
//        }

        return true;
    }

    public UUID getUUID() {
        return UUID.randomUUID();
    }
}