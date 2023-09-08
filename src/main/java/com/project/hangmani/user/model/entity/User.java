package com.project.hangmani.user.model.entity;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String id;
    private String gender;
    private String email;
    private String oAuthType;
    private String age;
    private String oAuthID;
    private String refreshToken;
}
