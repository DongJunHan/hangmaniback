package com.project.hangmani.controller;

import com.project.hangmani.convert.RequestConvert;
import com.project.hangmani.dto.UserDTO.RequestInsertUserDTO;
import com.project.hangmani.dto.UserDTO.ResponseUserDTO;
import com.project.hangmani.service.KakaoOAuthService;
import com.project.hangmani.service.OAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final KakaoOAuthService kakaoOAuthService;
    private static OAuthService oAuthService;
    private final RequestConvert requestConvert;
    private String oauth_type;

    public UserController(KakaoOAuthService kakaoOAuthService) {
        this.requestConvert = new RequestConvert();
        this.kakaoOAuthService = kakaoOAuthService;
    }
    @GetMapping("/{oauth_type}")
    public String login(@PathVariable("oauth_type") String oauth_type) {
        if (oauth_type.equals("kakao")) {
            this.oAuthService = kakaoOAuthService;
            this.oauth_type = oauth_type;
            return "redirect:" + oAuthService.getAuthorizationUrl();
        }else{
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

        Map<String, Object> respTable = oAuthService.getAccessToken(code);
        Map<String, Object> kakaoUserInfo = oAuthService.getUserInfo(respTable);

        RequestInsertUserDTO userDTO = requestConvert.convertDTO(respTable, kakaoUserInfo, this.oauth_type);
        ResponseUserDTO responseUserDTO = oAuthService.InsertUser(userDTO);

        session.setAttribute("user_id", responseUserDTO.getId());
        session.setAttribute("refresh_token", responseUserDTO.getRefreshToken());
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @DeleteMapping
    public String linkOut(@SessionAttribute("refresh_token") String refreshToken,
                          @SessionAttribute("user_id") String userID,
                          HttpSession session) {
        log.info("refresh_token={}", refreshToken);
        oAuthService.deleteUser(refreshToken, userID);
        session.removeAttribute("refresh_token");
        session.removeAttribute("user_id");
        return "redirect:/";
    }
}