package com.project.hangmani.security;

import jakarta.annotation.PostConstruct;

import java.nio.charset.Charset;

public interface TwoWayEncryptionInterface {

    byte[] encryptData(String text, Charset encode);
    byte[] decryptData(byte[] encryptData);
    byte[] createKey(int keySize);
    void init();
}
