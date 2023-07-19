package com.project.hangmani.config;

import org.springframework.beans.factory.annotation.Value;

public class OAuthConst {
    public static final String CLIENT_ID = "eec30559b1482179d83dd69beb47a816";
    private static final String LOGIN_REDIRECT_URI = "http://localhost:9090/user";
    public static final String URI = "/oauth/token";
    public static final String USER_INFO_URI = "/v2/user/me";
    public static final String KAUTH_HOST = "kauth.kakao.com";
    public static final String KAPI_HOST = "kapi.kakao.com";
    public static final String UNLINK_URI = "/v1/user/unlink";
    public static final String POST_REDIRECT_URI = "http://localhost:9090/user";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String LOGIN_URL = "/oauth/authorize?client_id="+CLIENT_ID+"&response_type=code&redirect_uri="+LOGIN_REDIRECT_URI+"&scope=";
    public static final String HTTPS = "https://";
    public static final String KAKAO_SCOPE = "account_email,gender,age_range,birthday,talk_message";
    public static final String MARKER_API_URL = "naveropenapi.apigw.ntruss.com";
    public static final String MARKER_API_URI = "/map-static/v2/raster";


}
