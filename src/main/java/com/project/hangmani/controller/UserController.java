package com.project.hangmani.controller;

import com.project.hangmani.convert.RequestConvert;
import com.project.hangmani.dto.UserDTO.RequestInsertUserDTO;
import com.project.hangmani.dto.UserDTO.ResponseUserDTO;
import com.project.hangmani.service.OAuthService;
import com.project.hangmani.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.project.hangmani.config.OAuthConst.KAKAO_SCOPE;
import static com.project.hangmani.config.OAuthConst.KAUTH_HOST;


@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
//    private final GoogleOAuthService googleOAuthService;
    private final OAuthService oAuthService;
    private final RequestConvert requestConvert;
    private final UserService userService;
    private String oauth_type;

    public UserController(OAuthService oAuthService, UserService userService) {
        this.requestConvert = new RequestConvert();
        this.oAuthService = oAuthService;
        this.userService = userService;
//        this.googleOAuthService = googleOAuthService;
    }
    @GetMapping("/{oauth_type}")
    public String login(@PathVariable("oauth_type") String oauth_type) {
        if (oauth_type.equals("kakao")) {
//            this.oAuthService = kakaoOAuthService;
//            this.oauth_type = oauth_type;
            return "redirect:" + oAuthService.getAuthorizationUrl(KAUTH_HOST, KAKAO_SCOPE);

//        }else if() {
//            this.oAuthService = googleOAuthService;
//            this.oauth_type = oauth_type;

        }
        else{
            return "redirect:/error";
        }

    }

    @GetMapping
    public String getCode(@RequestParam("code") String code,
                          @RequestParam(name = "state", defaultValue = "") String state,
                          @RequestParam(name = "error", defaultValue = "") String error,
                          @RequestParam(name = "error_description", defaultValue = "") String errorDescription,
                          HttpSession session) {
        log.info("code={}", code);
        log.info("state={}", state);
        log.info("error={}", error);
        log.info("errorDescription={}", errorDescription);
//        response={"access_token":"sDYY2gCTfYUbjh75nDpHuALiRkoVlgNQc6WkqgqeCj10mAAAAYb9mXwR","token_type":"bearer","refresh_token":"0pZQYURVwcIGntLJA8ATxCCHlM5dfNLUOFomK35xCj10mAAAAYb9mXwQ","expires_in":21599,"scope":"age_range birthday account_email talk_message gender","refresh_token_expires_in":5183999}
        Map<String, Object> respTable = oAuthService.getAccessTokenByCode(code);
//        kakoUserInfo={"id":1235586990,"connected_at":"2023-03-18T08:03:31Z","kakao_account":{"profile_needs_agreement":true,"has_email":true,"email_needs_agreement":false,"is_email_valid":true,"is_email_verified":true,"email":"gamedokdok@naver.com","has_age_range":true,"age_range_needs_agreement":false,"age_range":"30~39","has_birthday":true,"birthday_needs_agreement":false,"birthday":"1028","birthday_type":"SOLAR","has_gender":true,"gender_needs_agreement":false,"gender":"male"}}
        Map<String, Object> kakaoUserInfo = oAuthService.getUserInfo(respTable);

        RequestInsertUserDTO userDTO = requestConvert.convertDTO(respTable, kakaoUserInfo, this.oauth_type);
        ResponseUserDTO responseUserDTO = userService.InsertUser(userDTO);

        session.setAttribute("userDTO", responseUserDTO);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @DeleteMapping
    public String linkOut(@SessionAttribute("userDTO") ResponseUserDTO userDTO,
                          HttpSession session) {
        log.info("refresh_token={}", userDTO.getRefreshToken());
        //TODO getACcessToken 분리
        String accessToken = oAuthService.getAccessTokenByRefreshToken(userDTO.getRefreshToken());
        userService.deleteUser(userDTO.getId());
        oAuthService.unlinkUserInfo(accessToken);

        session.removeAttribute("refresh_token");
        session.removeAttribute("user_id");
        return "redirect:/";
    }
}