package com.project.hangmani.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hangmani.exception.StringToJsonException;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class ConvertData {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String DAOToJson(Object dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> JsonToMap(String data) {
        try {
            return objectMapper.readValue(data, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new StringToJsonException("string to json data convert fail",e);
        }

    }

    public java.sql.Date getSqlDate() {
        Date date = new Date();
        long time = date.getTime();
        return new java.sql.Date(time);
    }
    public java.sql.Date addSecondsCurrentDate(long seconds) {
        Date date = new Date();
        long futureTime = date.getTime() + (seconds * 1000);
        return new java.sql.Date(futureTime);

    }
    public java.sql.Date convertSqlDate(long timeStamp) {
        Timestamp timestamp = new Timestamp(timeStamp * 1000);
        return new java.sql.Date(timestamp.getTime());
    }

    public String byteToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
    public String byteToBase64(byte[] input) {
        // Convert encrypted bytes to Base64 encoded string
        return Base64.getEncoder().encodeToString(input);
    }

    public byte[] base64ToByte(String base64) {
        Util util = new Util();
        //check input data
        if (!util.isBase64(base64))
            return null;

        // Convert decrypt base64 to byte[]
        return Base64.getDecoder().decode(base64);
    }

    public String byteToString(byte[] input, Charset encoding) {
        return new String(input, encoding);
    }

    public byte[] stringToByte(String input, Charset encoding) {
        return input.getBytes(encoding);
    }
}
