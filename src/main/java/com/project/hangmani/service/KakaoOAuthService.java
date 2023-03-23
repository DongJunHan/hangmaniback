package com.project.hangmani.service;

import com.project.hangmani.domain.User;
import com.project.hangmani.dto.UserDTO.RequestInsertUserDTO;
import com.project.hangmani.exception.*;
import com.project.hangmani.repository.UserRepository;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
    public ResponseUserDTO InsertUser(RequestInsertUserDTO requestInsertUserDTO) {
        //encrypt, encoding id data
        byte[] encryptOAuthID = this.aes.encryptData(requestInsertUserDTO.getOAuthID(), StandardCharsets.UTF_8);
        String base64OAuthID = convertData.byteToBase64(encryptOAuthID);
        //confirm exist user @Valid
        Optional<User> users = userRepository.findByoAuthId(base64OAuthID);
        if (!users.isEmpty()){
            User user = users.get();
            return ResponseUserDTO.builder()
                    .id(user.getId())
                    .refreshToken(user.getRefreshToken())
                    .oAuthType(user.getOAuthType())
                    .build();
        }
        log.info("restDTO={}",requestInsertUserDTO.toString());
        //insert user
        String userID = userRepository.insertUser(requestInsertUserDTO);
        if (userID.isBlank())
            throw new FailInsertData();

        //check user data
        Optional<User> findUser = userRepository.findById(userID);
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
    public void deleteUser(String refreshToken, String userID) {
        //check id valid
        Optional<User> usersByID = userRepository.findById(userID);
        if (usersByID.isEmpty()){
            throw new NotFoundUser();
        }

        //get access_token
        String accessToken = getAccessTokenByRefreshToken(refreshToken);
        //session unlink
        String oAuthID = unlinkUserInfo(accessToken);
        //check id validation
        Optional<User> users = userRepository.findByoAuthId(oAuthID);
        if (users.isEmpty()){
            throw new NotFoundUser();
        }
        User user = users.get();

        //TODO if return id , parameter id not equal
//        if (!base64OAuthID.equals(user.getOAuthID()))
//            throw new NotFoundUser();

        //delete db
        int ret = userRepository.deleteUser(user.getId());
        if (ret != 3)
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
    private String getAccessTokenByRefreshToken(String refreshToken) {
        Util util = new Util();
        String responseBody;
        // check refresh token decoding, decrypt refresh token
        if (util.isBase64(refreshToken)){
            byte[] encryptToken = this.convertData.base64ToByte(refreshToken);
            String decryptData = this.convertData.byteToString(this.aes.decryptData(encryptToken),
                    StandardCharsets.UTF_8);
            responseBody = webClient.post().uri(builder -> builder.scheme("https")
                            .host(KAUTH_HOST)
                            .path(URI)
                            .queryParam("grant_type", "refresh_token")
                            .queryParam("client_id", CLIENT_ID)
                            .queryParam("refresh_token", decryptData)
                            .build())
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .syncBody("")
                    .exchange()
                    .block()
                    .bodyToMono(String.class)
                    .block();
        }else{
            responseBody = webClient.post().uri(builder -> builder.scheme("https")
                            .host(KAUTH_HOST)
                            .path(URI)
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
        }
        log.info("result={}",responseBody);
        if (responseBody == null)
            throw new KakaoAuthException();
        Map<String, Object> respTable = convertData.JsonToMap(responseBody);

        return respTable.get("access_token").toString();
    }
}
