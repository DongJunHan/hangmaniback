package com.project.hangmani.config;

public class OAuthConst {
    public static final String CLIENT_ID = "eec30559b1482179d83dd69beb47a816";
    private static final String LOGIN_REDIRECT_URI = "http://localhost:8080/user";
    public static final String URI = "/oauth/token";
    public static final String USER_INFO_URI = "/v2/user/me";
    public static final String HOST = "kauth.kakao.com";
    public static final String USER_INFO_HOST = "kapi.kakao.com";
    public static final String POST_REDIRECT_URI = "http://localhost:8080/user";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String KAKAO_LOGIN_URL = "https://kauth.kakao.com/oauth/authorize?client_id="+CLIENT_ID+"&redirect_uri="+LOGIN_REDIRECT_URI+"&response_type=code&scope=account_email,gender,age_range,birthday,talk_message";

}
