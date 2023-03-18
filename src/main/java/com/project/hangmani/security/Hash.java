package com.project.hangmani.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    public String sha256Hash(String text) throws NoSuchAlgorithmException {
        // SHA-256 해시 알고리즘을 선택하여 MessageDigest 인스턴스 생성
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        // 입력 데이터의 바이트 배열을 해시 계산에 전달
        byte[] hashBytes = md.digest(text.getBytes());
        // 바이트 배열을 16진수 문자열로 변환하여 출력
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();

    }
}