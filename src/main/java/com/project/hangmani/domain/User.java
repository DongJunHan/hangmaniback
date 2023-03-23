package com.project.hangmani.domain;

import lombok.*;

import java.sql.Date;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String refreshToken;
    private Date refreshTokenExpire;
    private String id;
    private String gender;
    private String email;
    private String oAuthType;
    private String age;
    private String oAuthID;
}
