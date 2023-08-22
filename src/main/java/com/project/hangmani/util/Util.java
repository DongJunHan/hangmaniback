package com.project.hangmani.util;

import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.exception.FileIOException;
import com.project.hangmani.exception.IO;
import com.project.hangmani.exception.InvalidKeySpec;
import com.project.hangmani.exception.NoSuchAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.project.hangmani.config.SecurityConst.*;
@Slf4j
public class Util {
    PropertiesValues propertiesValues;

    public Util(PropertiesValues propertiesValues) {
        this.propertiesValues = propertiesValues;
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

    public String getRenameWithDate(Date date, int randomSeed, String originalFileName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddSSS");
        String formattedNow = dateFormat.format(date); // 년도, 월, 일, 밀리초 형식의 문자열로 변환


        int lastDotIndex = originalFileName.lastIndexOf(".");
        String extension;
        if (lastDotIndex >= 0) {
            extension = originalFileName.substring(lastDotIndex);
        }else{
            extension = originalFileName;
        }
        return formattedNow + "_" + randomSeed + extension;
    }
    public int getRandomValue() {
        Random random = new Random();
        return random.nextInt(10000);
    }

    public String generateFileUrl(String savedFileName) {
        return propertiesValues.getDomain() + savedFileName;
    }
    public boolean checkDirectory(Path path) throws FileIOException, IOException {
        Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);

        File file = path.toFile();
        if (!file.exists()) {
            String[] splitPath = propertiesValues.getUploadDir().split("/");
            String addPath = "";
            for (String subPath :
                    splitPath) {
                if (subPath.length() == 0)
                    continue;
                addPath =addPath+ "/" + subPath;
                File f = new File(addPath);
                log.info("add={}", addPath);
                Path of = Path.of(addPath);
                try {
                    if (!f.exists()) {
                        log.info("create directory and add permissions");
                        Files.createDirectories(of);
                        Files.setPosixFilePermissions(of, perms);
                    }else{
                        log.info("add permissions");
                        Files.setPosixFilePermissions(of, perms);
                    }
                } catch (AccessDeniedException e){
                    throw new FileIOException("directory access denied", e);
//                } catch (IOException ex) {
//                    throw new FileIOException("sub mkdir fail", ex);
                }
            }
        }
        return true;
    }
    public CompletableFuture<Void> savedAttachmentFile(InputStream inputStream, String savedFileName) {
        if (null == propertiesValues.getUploadDir())
            return null;
        Path path = Paths.get(propertiesValues.getUploadDir());
        log.info("file name: {}", path.getFileName());
        try {
            checkDirectory(path);
        } catch (IOException e) {
            log.error("FileSystemException ", e);
            return null;
//            throw new RuntimeException(e);
        }
        Path filePath = Paths.get(propertiesValues.getUploadDir() + savedFileName);
        CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
            try (OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW)) {
                byte[] chunk = new byte[4096];
                int len;
                while ((len = inputStream.read(chunk)) > 0) {
                    outputStream.write(chunk, 0, len);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }

    public static Double getDistance(Double userLatitude, Double userLongitude, Double targetLatitude, Double targetLongitude) {
        Double earthRadius = 6371.;
        Double dLat = Math.toRadians(targetLatitude - userLatitude);
        Double dLon = Math.toRadians(targetLongitude - userLongitude);
        userLatitude = Math.toRadians(userLatitude);
        targetLatitude = Math.toRadians(targetLatitude);

        Double alpha =  (Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(userLatitude) * Math.cos(targetLatitude) * Math.sin(dLon/2) * Math.sin(dLon/2));
        Double centralAngle = (2 * Math.atan2(Math.sqrt(alpha), Math.sqrt(1-alpha)));

        return earthRadius * centralAngle;
    }

    public java.sql.Date getSqlDate() {
        java.util.Date date = new java.util.Date();
        long time = date.getTime();
        return new java.sql.Date(time);
    }

}