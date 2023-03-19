package com.project.hangmani.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hangmani.exception.StringToJsonException;

import java.sql.Timestamp;
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

    public Map<String, String> JsonToMap(String data) {
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
}
