package com.project.hangmani.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDTO {
    private String id;
    private String oAuthType;
    private String refreshToken;
}
