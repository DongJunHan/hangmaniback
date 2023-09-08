package com.project.hangmani.user.model.dto;

import lombok.*;


@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String gender;
    private String email;
    private String oAuthType;
    private String age;
    private String oAuthID;
    private String refreshToken;
}
