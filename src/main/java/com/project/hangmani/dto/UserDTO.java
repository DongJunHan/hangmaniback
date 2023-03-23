package com.project.hangmani.dto;

import lombok.*;

import java.sql.Date;


public class UserDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class RequestInsertUserDTO {
        private String id;
        private String refreshToken;
        private Date refreshTokenExpire;
        private String email;
        private String age;
        private String  gender;
        private String oAuthType;
        private String oAuthID;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResponseUserDTO {
        private String id;
        private String oAuthType;
        private String refreshToken;
    }

}
