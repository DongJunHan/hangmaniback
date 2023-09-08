package com.project.hangmani.user.model.dto;

import com.project.hangmani.user.model.entity.User;
import com.project.hangmani.util.ConvertData;
import lombok.*;

import java.sql.Date;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class RequestInsertDTO {
    private String id;
    private String refreshToken;
    private Date refreshTokenExpire;
    private String email;
    private String age;
    private String gender;
    private String oAuthType;
    private String oAuthID;
    private ConvertData convertData;
    public RequestInsertDTO(ConvertData convertData) {
        this.convertData = convertData;
    }

    public User convertToEntity() {
        return User.builder()
                .age(this.getAge())
                .email(this.getEmail())
                .refreshToken(this.getRefreshToken())
                .gender(this.getGender())
                .oAuthID(this.getOAuthID())
                .oAuthType(this.getOAuthType())
                .build();
    }
    public RequestInsertDTO convertToDTO(Map<String, Object> tokenInput,
                                         Map<String, Object> userInfo,
                                         String oAuthType) {
        Date refreshTokenExpiresIn = this.convertData.addSecondsCurrentDate((int) tokenInput.get("refresh_token_expires_in"));
        String oAuthID = userInfo.get("id").toString();
        Map<String, Object> scope = (Map<String, Object>) userInfo.get("kakao_account");
        return RequestInsertDTO.builder()
                .oAuthID(oAuthID)
                .refreshToken((String)tokenInput.get("refresh_token"))
                .refreshTokenExpire(refreshTokenExpiresIn)
                .age(scope.get("age_range").toString())
                .email(scope.get("email").toString())
                .gender(scope.get("gender").toString())
                .oAuthType(oAuthType)
                .build();

    }

}
