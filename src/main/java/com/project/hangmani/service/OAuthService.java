package com.project.hangmani.service;

import com.project.hangmani.dto.UserDTO.RequestInsertUserDTO;

import java.util.Map;

import static com.project.hangmani.dto.UserDTO.*;

public interface OAuthService {
    Map<String, Object>  getAccessToken(String code);
    Map<String, Object> getUserInfo(Map<String, Object> tokenInfos);
    ResponseUserDTO InsertUser(RequestInsertUserDTO requestInsertUserDTO);
    String getAuthorizationUrl();
    void deleteUser(String token, String userID);
}
