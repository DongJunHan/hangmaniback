package com.project.hangmani.security;

import com.project.hangmani.exception.InvalidKeySpec;
import com.project.hangmani.exception.NoSuchAlgorithm;
import com.project.hangmani.exception.NullPointKey;
import com.project.hangmani.util.Util;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static com.project.hangmani.config.SecurityConst.ALGORITHM;

@Slf4j
public class RSA implements TwoWayEncryptionInterface{
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Util util;
    @PostConstruct
    public void init() {
        log.info("init start");
//        util = new Util();
//        this.password = util.getKeyFromFile();
    }

    @Override
    public byte[] decryptData(byte[] encryptData) {
        return new byte[0];
    }

    @Override
    public byte[] createKey(int keySize) {
        return new byte[0];
    }

    @Override
    public byte[] encryptData(String text, Charset encode) {
        if (this.publicKey == null || this.privateKey == null) {
            throw new NullPointKey();
        }
        try {
            // RSA 암호화
            byte[] data = text.getBytes("UTF-8");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
            byte[] encryptedData = cipher.doFinal(data);
            return encryptedData;
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithm("encryptData", e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
    public String decryptInfo(){
        // RSA 복호화
//        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        byte[] decryptedData = cipher.doFinal(encryptedData);
//        System.out.println("Decrypted data: " + new String(decryptedData, "UTF-8"));
        return null;
    }
    private void getPublicKey() {
        if (this.publicKey != null)
            return;
        PublicKey publicKey = util.getPublicKeyFromFile();
        this.publicKey = publicKey;
    }


    private void getPrivateKey() {
        if (this.privateKey != null)
            return;
//        PrivateKey privateKey = util.getPrivateKeyFromFile(alias, password);
        this.privateKey = privateKey;

    }
    private void getPublicKeyByPrivateKey() {
        byte[] privateKeyBytes = privateKey.getEncoded();
        try {
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
            PublicKey publicKey = factory.generatePublic(new X509EncodedKeySpec(privateKey.getEncoded()));
            this.publicKey = publicKey;
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithm("getPublicKeyByPrivateKey",e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("getPublicKeyByPrivateKey",e);
        }
    }
    private void getPrivateKeyByPublicKey() {
        byte[] publicKeyBytes = this.publicKey.getEncoded();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(publicKeyBytes);
        try{
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
            factory.generatePrivate(spec);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithm("getPrivateKeyByPublicKey", e);
        } catch (InvalidKeySpecException e) {
            throw new InvalidKeySpec("getPrivateKeyByPublicKey", e);
        }
    }

    private KeyPair createKeyPair(String algorithm, int keySize) {
        try {
            // RSA key pair 생성
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
            SecureRandom random = new SecureRandom();
            keyPairGen.initialize(keySize, random);
            return keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithm("createKeyPair",e);
        }
    }
}
