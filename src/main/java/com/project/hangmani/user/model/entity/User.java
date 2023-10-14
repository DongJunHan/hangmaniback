package com.project.hangmani.user.model.entity;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    private String id;
    private String gender;
    private String email;
    private String oAuthType;
    private String age;
    private String oAuthID;
    private String refreshToken;
    @Builder
    private User(String id, String gender, String email, String oAuthType, String age, String oAuthID, String refreshToken) {
        this.id = id;
        this.gender = gender;
        this.email = email;
        this.oAuthType = oAuthType;
        this.age = age;
        this.oAuthID = oAuthID;
        this.refreshToken = refreshToken;
    }
}
