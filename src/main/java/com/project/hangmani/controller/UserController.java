package com.project.hangmani.controller;

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
    private final OAuthService oAuthService;

    public UserController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
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

        Map<String, String> respTable = oAuthService.getAccessToken(code);
        String kakaoUserInfo = oAuthService.getKakaoUserInfo(respTable);
        log.info("kakoUserInfo={}",kakaoUserInfo);
        model.addAttribute("access_token", respTable.get("access_token"));
        oAuthService.InsertKaKaoUser(kakaoUserInfo, respTable.get("access_token"));
        return "redirect:/basic/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}