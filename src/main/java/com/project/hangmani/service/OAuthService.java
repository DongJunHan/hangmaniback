package com.project.hangmani.service;

import com.project.hangmani.exception.KO310Exception;
import com.project.hangmani.exception.KakaoAuthException;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import io.netty.handler.codec.http.HttpScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.project.hangmani.config.OAuthConst.*;

@Service
@Slf4j
public class OAuthService implements OAuthInterface{
    private WebClient webClient;
    private ConvertData convertData;
    private Util util;
    private AES aes;


    public OAuthService(WebClient webClient, Util util, AES aes) {
        this.webClient = webClient;
        this.convertData = new ConvertData();
        this.util = util;
        this.aes = aes;
    }

    @Override
    public Map<String, Object> getAccessTokenByCode(String code) {
        String responseBody = webClient.post()
                .uri(builder -> builder.scheme(String.valueOf(HttpScheme.HTTPS))
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

    @Override
    public String getAuthorizationUrl() {
        return HTTPS + KAUTH_HOST + LOGIN_URL + KAKAO_SCOPE;
    }

    @Override
    public String getAccessTokenByRefreshToken(String refreshToken) {
        String responseBody;
        // check refresh token decoding, decrypt refresh token
        if (this.util.isBase64(refreshToken)){
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

    @Override
    public String unlinkUserInfo(String accessToken) {
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
}
