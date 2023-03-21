package com.project.hangmani.controller;

import com.project.hangmani.convert.RequestConvert;
import com.project.hangmani.dto.UserDTO.RequestInsertOAuthDTO;
import com.project.hangmani.dto.UserDTO.RequestInsertScopeDTO;
import com.project.hangmani.dto.UserDTO.RequestInsertUserDTO;
import com.project.hangmani.dto.UserDTO.ResponseUserDTO;
import com.project.hangmani.service.KakaoOAuthService;
import com.project.hangmani.service.OAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
                          Model model) {
        log.info("code={}", code);
        log.info("state={}", state);
        log.info("error={}", error);
        log.info("errorDescription={}", errorDescription);

        Map<String, Object> respTable = oAuthService.getAccessToken(code);
        Map<String, Object> kakaoUserInfo = oAuthService.getUserInfo(respTable);

        RequestInsertUserDTO userDTO = requestConvert.convertDTO(respTable, kakaoUserInfo.get("id").toString());
        RequestInsertScopeDTO scopeDTO = requestConvert.convertDTO(kakaoUserInfo);
        RequestInsertOAuthDTO oAuthDTO = requestConvert.convertDTO(kakaoUserInfo.get("id").toString(), this.oauth_type);

        ResponseUserDTO responseUserDTO = oAuthService.InsertUser(userDTO, scopeDTO, oAuthDTO);

        model.addAttribute("responseUserDTO", responseUserDTO);
        return "redirect:/basic/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}