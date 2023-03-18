package com.project.hangmani.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class UserDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestUserDTO {
        private String accessToken;
        private String tokenType;
        private String refreshToken;
        private long expiresIn;
        private String scope;
        private long refreshTokenExpiresIn;

    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestAuthDTO {
        private String tokenType;
        private String accessToken;
        private String idToken;
        private long expiresIn;
        private String refreshToken;
        private String refreshTokenExpiresIn;
        private String scope;


    }



}
