package com.project.hangmani.service;

import com.project.hangmani.domain.User;
import com.project.hangmani.repository.UserRepository;
import com.project.hangmani.util.ConvertData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Date;
import java.util.Map;

import static com.project.hangmani.config.OAuthConst.*;


@Service
@Slf4j
public class OAuthService {
    private UserRepository userRepository;
    private ConvertData convertData;

    public OAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.convertData = new ConvertData();
    }

    public String redirectPostKakao(String code) {
//        response_type=code&client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}
        WebClient client = WebClient.create();
        String response = client.post()
                .uri(builder -> builder.scheme("https")
                        .host(HOST)
                        .path(URI)
                        .queryParam("response_type","code")
//                        .queryParam("grant_type",GRANT_TYPE)
                        .queryParam("client_id", CLIENT_ID)
                        .queryParam("redirect_uri",POST_REDIRECT_URI)
//                        .queryParam("code", code)
//                        .queryParam("client_secret","")
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .syncBody("")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("response={}", response);
//        Map<String, String> respTable = convertData.JsonToMap(response);
//        log.info("table={}",respTable);
        return "basic/login";
    }

//    private User InsertUser(String token, String refreshToken, int tokenExpire, int refreshTokenExpire, String option){
//        Date convertTokenExpire = convertData.convertSqlDate((long) tokenExpire);
//        Date convertRefreshTokenExpire = convertData.convertSqlDate((long) refreshTokenExpire);
//        int ret = userRepository.insertUser(token, refreshToken, convertTokenExpire, convertRefreshTokenExpire, option);
//
//    }
}
