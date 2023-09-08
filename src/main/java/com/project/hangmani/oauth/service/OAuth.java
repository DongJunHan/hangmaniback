package com.project.hangmani.oauth.service;

import java.util.Map;

public interface OAuth {
    Map<String, Object>  getAccessTokenByCode(String code);
    Map<String, Object> getUserInfo(Map<String, Object> tokenInfos);
    String getAuthorizationUrl();
    String getAccessTokenByRefreshToken(String refreshToken);

    String unlinkUserInfo(String accessToken);

    String getType();
}
