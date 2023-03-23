package com.project.hangmani.security;

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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;

import static com.project.hangmani.config.SecurityConst.*;

@Slf4j
@Component
public class AES {
    private static byte[] key;
    @PostConstruct
    public void init() {
        log.info("start");
        Util util = new Util();

        this.key = util.getKeyFromFile();
        if(this.key == null) {
            this.key = createAESKey(KEY_SIZE);
            util.saveByteData(this.key);
        }
        log.info("this.key={}", this.key);
    }


    public byte[] encryptData(String text, Charset encode) {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        ConvertData convertData = new ConvertData();
        // Encrypt plaintext®
        try {
            return cipher.doFinal(convertData.stringToByte(text, encode));
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] decryptData(byte[] encryptData) {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
        try {
            return cipher.doFinal(encryptData);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    private Cipher getCipher(int mode) {
        if (key == null && key.length == 0)
            throw new NullPointKey();
        // Create secret key
        SecretKeySpec secretKeySpec = new SecretKeySpec(this.key, ALGORITHM);
        // Create cipher
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(mode, secretKeySpec);
            return cipher;
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithm("getCipher", e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new InvalidKeySpec("getCipher",e);
        }
    }

    private byte[] createAESKey(int keySize) {
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
}

