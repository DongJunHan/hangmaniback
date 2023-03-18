package com.project.hangmani.service;

import com.project.hangmani.exception.KO310Exception;
import com.project.hangmani.repository.UserRepository;
import com.project.hangmani.util.ConvertData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static com.project.hangmani.config.OAuthConst.*;


@Service
@Slf4j
public class OAuthService {
    private UserRepository userRepository;
    private WebClient webClient;
    private ConvertData convertData;

    public OAuthService(UserRepository userRepository, WebClient webClient) {
        this.userRepository = userRepository;
        this.webClient = webClient;
        this.convertData = new ConvertData();
    }

    public Map<String, String> getAccessToken(String code) {
//        response_type=code&client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}

        String responseBody = webClient.post()
                .uri(builder -> builder.scheme("https")
                        .host(HOST)
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
        Map<String, String> respTable = convertData.JsonToMap(responseBody);
        if (respTable.containsKey("error")) {
            if (respTable.get("error_code") == "KOE310"){
                throw new KO310Exception(respTable.get("error_description"));
            } else if (respTable.get("error_code") == "KOE303") {
                throw new KO310Exception(respTable.get("error_description"));
            }
        }

        return respTable;
    }

    public String getKakaoUserInfo(Map<String, String> tokenInfos) {
        log.info("access_token={}",tokenInfos.get("access_token"));
        String responseBody = webClient.get()
                .uri(builder -> builder.scheme("https")
                        .host(HOST)
                        .path(USER_INFO_URI)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenInfos.get("access_token"))
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();
        log.info("response={}",responseBody);
        return responseBody;
    }
}
