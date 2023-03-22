package com.project.hangmani.service;

import com.project.hangmani.domain.User;
import com.project.hangmani.dto.UserDTO.RequestInsertUserDTO;
import com.project.hangmani.exception.*;
import com.project.hangmani.repository.UserRepository;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.ConvertData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Date;
import java.util.Map;
import java.util.Optional;

import static com.project.hangmani.config.OAuthConst.*;
import static com.project.hangmani.dto.UserDTO.*;


@Service
@Slf4j
public class KakaoOAuthService implements OAuthService{
    private UserRepository userRepository;
    private WebClient webClient;
    private ConvertData convertData;
    private AES aes;

    public KakaoOAuthService(UserRepository userRepository, WebClient webClient) {
        this.userRepository = userRepository;
        this.webClient = webClient;
        this.convertData = new ConvertData();
        aes = new AES();
    }
    @Override
    public Map<String, Object> getAccessToken(String code) {
        String responseBody = webClient.post()
                .uri(builder -> builder.scheme("https")
                        .host(KAUTH_HOST)
                        .path(URI)
                        .queryParam("grant_type", GRANT_TYPE)
                        .queryParam("client_id", CLIENT_ID)
                        .queryParam("redirect_uri", POST_REDIRECT_URI)
                        .queryParam("code", code)
                        .queryParam("client_secret", "")
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .syncBody("")
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();
        log.info("response={}", responseBody);
        Map<String, Object> respTable = convertData.JsonToMap(responseBody);
        if (respTable.containsKey("error")) {
            if (respTable.get("error_code") == "KOE310"){
                throw new KO310Exception(respTable.get("error_description").toString());
            } else if (respTable.get("error_code") == "KOE303") {
                throw new KO310Exception(respTable.get("error_description").toString());
            }
        }

        return respTable;
    }
    @Override
    public Map<String, Object> getUserInfo(Map<String, Object> tokenInfos) {
        log.info("access_token={}",tokenInfos.get("access_token"));
        String responseBody = webClient.get()
                .uri(builder -> builder.scheme("https")
                        .host(KAPI_HOST)
                        .path(USER_INFO_URI)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenInfos.get("access_token"))
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();
        return convertData.JsonToMap(responseBody);
    }
    @Transactional
    @Override
    public ResponseUserDTO InsertUser(RequestInsertUserDTO requestInsertUserDTO,
                           RequestInsertScopeDTO requestInsertScopeDTO,
                           RequestInsertOAuthDTO requestInsertOAuthDTO) {
        //encrypt, encoding id data
        byte[] encryptID = aes.encryptData(requestInsertScopeDTO.getId());
        String base64ID = convertData.byteToBase64(encryptID);
        //confirm exist user
        Optional<User> users = userRepository.findById(base64ID);
        if (!users.isEmpty()){
            User user = users.get();
            return ResponseUserDTO.builder()
                    .id(user.getId())
                    .refreshToken(user.getRefreshToken())
                    .oAuthType(user.getOAuthType())
                    .build();
        }

        //set id
        requestInsertUserDTO.setId(base64ID);
        requestInsertScopeDTO.setId(base64ID);
        requestInsertOAuthDTO.setId(base64ID);


        //insert user
        int ret = userRepository.insertUser(requestInsertUserDTO);
        if (ret == 0)
            throw new FailInsertData();
        //insert scope
        ret = userRepository.insertScope(requestInsertScopeDTO);
        if (ret == 0)
            throw new FailInsertData();

        //insert oauth type
        ret = userRepository.insertOAuthType(requestInsertOAuthDTO);
        if (ret == 0)
            throw new FailInsertData();

        //get user data
        Optional<User> findUser = userRepository.findById(base64ID);
        if (findUser.isEmpty())
            throw new FailInsertData();

        User user = findUser.get();
        return ResponseUserDTO.builder()
                .id(user.getId())
                .refreshToken(user.getRefreshToken())
                .oAuthType(user.getOAuthType())
                .build();
    }

    @Override
    public String getAuthorizationUrl() {
        return KAKAO_LOGIN_URL;
    }

    @Transactional
    @Override
    public void deleteUser(String id) {
        //check id
        Optional<User> retUser = userRepository.findById(id);
        if (retUser.isEmpty())
            throw new NotFoundUser();

        User user = retUser.get();
        //get access_token
        String accessToken = getAccessTokenByRefreshToken(user.getRefreshToken(), id);
        //session unlink
        String req_id = unlinkUserInfo(accessToken);
        //check id validation
        byte[] encryptData = this.aes.encryptData(req_id);
        String base64Id = convertData.byteToBase64(encryptData);
        //TODO if return id , parameter id not equal
        if (!base64Id.equals(id))
            throw new NotFoundUser();

        //delete db
        int ret = userRepository.deleteUser(id);
        if (ret < 3)
            throw new FailInsertData();
    }

    private String unlinkUserInfo(String accessToken) {
        String responseBody = webClient.post().uri(builder -> builder.scheme("https")
                        .host(KAPI_HOST)
                        .path(UNLINK_URI)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .syncBody("")
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();
        Map<String, Object> respTable = convertData.JsonToMap(responseBody);
        return respTable.get("id").toString();

    }
    private String getAccessTokenByRefreshToken(String refreshToken, String id) {
        String responseBody = webClient.post().uri(builder -> builder.scheme("https")
                        .host(KAUTH_HOST)
                        .path(USER_INFO_URI)
                        .queryParam("grant_type", "refresh_token")
                        .queryParam("client_id", CLIENT_ID)
                        .queryParam("refresh_token", refreshToken)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .syncBody("")
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();

        Map<String, Object> respTable = convertData.JsonToMap(responseBody);

        //check refresh token
        if (!refreshToken.equals(respTable.get("refresh_token").toString())) {
            Date expiresIn = convertData.addSecondsCurrentDate((int) respTable.get("refresh_token_expires_in"));
            //update refresh token
            int ret = userRepository.updateRefreshToken(respTable.get("refresh_token").toString(), expiresIn, id);
            if (ret == 0)
                throw new FailUpdateData();
        }
        return respTable.get("access_token").toString();
    }
}
