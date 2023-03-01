package com.project.hangmani.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hangmani.exception.GeneralException;

import java.util.Date;

public class ConvertData {
    private ObjectMapper objectMapper = new ObjectMapper();
    public String DAOToJson(Object dto){
        try {
            String result = objectMapper.writeValueAsString(dto);
            return result;
        } catch (JsonProcessingException e) {
            throw new GeneralException(e);
        }
    }
    public java.sql.Date getSqlDate() {
        Date date = new Date();
        long time = date.getTime();
        return new java.sql.Date(time);
    }
}
