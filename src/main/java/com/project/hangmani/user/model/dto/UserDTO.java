package com.project.hangmani.user.model.dto;

import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDTO {
    private String id;
    private String gender;
    private String email;
    private String oAuthType;
    private String age;
    private String oAuthID;
    private String refreshToken;
    @Builder
    private UserDTO(String id, String gender, String email,
                    String oAuthType, String age, String oAuthID,
                    String refreshToken) {
        this.id = id;
        this.gender = gender;
        this.email = email;
        this.oAuthType = oAuthType;
        this.age = age;
        this.oAuthID = oAuthID;
        this.refreshToken = refreshToken;
    }
}
