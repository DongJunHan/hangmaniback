package com.project.hangmani.security;

import com.project.hangmani.exception.IO;
import com.project.hangmani.exception.InvalidKeySpec;
import com.project.hangmani.exception.NoSuchAlgorithm;
import com.project.hangmani.exception.NullPointKey;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;

import static com.project.hangmani.config.SecurityConst.*;

@Slf4j
@Component
public class AES implements TwoWayEncryptionInterface {
    private static byte[] key;
    private Cipher encrpytCipher;
    private Cipher decryptCipher;
    Util util;
    public AES(Util util){
        this.util = util;
        init();
    }
    @Override
    public void init() {
        log.info("start");
//        if (this.key != null)
//            return;
        this.key = getKeyFromFile();

        //TODO config 파일 있는지 체크하는걸 서버가 띄워질 때 체크해야힘. 없으면 에러.
        if(this.key == null) {
            this.key = createKey(KEY_SIZE);
            util.saveByteData(this.key);
        }
        log.info("this.key={}", this.key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(this.key, ALGORITHM);
        try {
            this.encrpytCipher = Cipher.getInstance(CIPHER_ALGORITHM);
            this.encrpytCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            this.decryptCipher = Cipher.getInstance(CIPHER_ALGORITHM);
            this.decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithm("AES class", e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        log.info("al={}", encrpytCipher.getAlgorithm());
    }

    @Override
    public byte[] encryptData(String text, Charset encode) {
        if (this.encrpytCipher == null)
            throw new InvalidKeySpec();
        ConvertData convertData = new ConvertData();
        // Encrypt plaintext®
        try {
            return this.encrpytCipher.doFinal(convertData.stringToByte(text, encode));
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public byte[] decryptData(byte[] encryptData) {
        if (this.decryptCipher == null)
            throw new InvalidKeySpec();
        try {
            return decryptCipher.doFinal(encryptData);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

//    private Cipher getCipher(int mode) {
//        if (key == null && key.length == 0)
//            throw new NullPointKey();
//        // Create secret key
//        SecretKeySpec secretKeySpec = new SecretKeySpec(this.key, ALGORITHM);
//        // Create cipher
//        Cipher cipher;
//        try {
//            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
//            cipher.init(mode, secretKeySpec);
//            return cipher;
//        } catch (NoSuchAlgorithmException e) {
//            throw new NoSuchAlgorithm("getCipher", e);
//        } catch (NoSuchPaddingException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidKeyException e) {
//            throw new InvalidKeySpec("getCipher",e);
//        }
//    }
    @Override
    public byte[] createKey(int keySize) {
        if (keySize != 128 && keySize != 192 && keySize != 256) {
            throw new IllegalArgumentException("Key size must be 128, 192, or 256 bits");
        }
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    private byte[] getKeyFromFile() {
        Path path = Paths.get(KEY_FILE_PATH);
        File file = path.toFile();
        if (!file.exists())
            return null;
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new IO(e);
        }
    }}

