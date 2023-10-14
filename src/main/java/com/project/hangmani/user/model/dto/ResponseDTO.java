package com.project.hangmani.user.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDTO {
    private String id;
    private String oAuthType;
    private String refreshToken;
    @Builder
    private ResponseDTO(String id, String oAuthType, String refreshToken) {
        this.id = id;
        this.oAuthType = oAuthType;
        this.refreshToken = refreshToken;
    }
}
