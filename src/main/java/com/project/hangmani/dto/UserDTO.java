package com.project.hangmani.dto;

import lombok.*;

import java.sql.Date;


public class UserDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RequestInsertUserDTO {
        private String id;
        private String refreshToken;
        private Date refreshTokenExpire;

    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RequestInsertScopeDTO {
        private String id;
        private String email;
        private String oauthType;
        private String age;
        private String  gender;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RequestInsertOAuthDTO {
        private String id;
        private String oAuthType;
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
