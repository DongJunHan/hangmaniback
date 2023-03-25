package com.project.hangmani.service;

import com.project.hangmani.dto.UserDTO;

import java.util.Map;

public interface OAuthInterface {
    Map<String, Object>  getAccessTokenByCode(String code);
    Map<String, Object> getUserInfo(Map<String, Object> tokenInfos);
    String getAuthorizationUrl(String host, String scope);
    String getAccessTokenByRefreshToken(String refreshToken);

    String unlinkUserInfo(String accessToken);
}
