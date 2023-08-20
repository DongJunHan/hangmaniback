package com.project.hangmani.controller;

import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.convert.RequestConvert;
import com.project.hangmani.dto.UserDTO.RequestInsertUserDTO;
import com.project.hangmani.dto.UserDTO.ResponseUserDTO;
import com.project.hangmani.service.OAuthInterface;
import com.project.hangmani.service.OAuthService;
import com.project.hangmani.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.project.hangmani.config.OAuthConst.KAKAO_SCOPE;
import static com.project.hangmani.config.OAuthConst.KAUTH_HOST;


@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    private Map<String, OAuthInterface> oauthMap;
    private final RequestConvert requestConvert;
    private final UserService userService;
    private String oauthType;

    public UserController(OAuthService oAuthService, UserService userService, PropertiesValues propertiesValues) {
        this.requestConvert = new RequestConvert(propertiesValues);
        this.userService = userService;
        oauthMap = new HashMap<>();
        oauthMap.put("kakao", oAuthService);
    }
    @GetMapping("/{oauth_type}")
    public String login(@PathVariable("oauth_type") String oauth_type) {
        this.oauthType = oauth_type;
        return "redirect:" + oauthMap.get(oauth_type).getAuthorizationUrl();
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
        Map<String, Object> respTable = oauthMap.get(this.oauthType).getAccessTokenByCode(code);
        Map<String, Object> kakaoUserInfo = oauthMap.get(this.oauthType).getUserInfo(respTable);

        RequestInsertUserDTO userDTO = requestConvert.convertDTO(respTable, kakaoUserInfo, this.oauthType);
        ResponseUserDTO responseUserDTO = userService.InsertUser(userDTO);

        session.setAttribute("userDTO", responseUserDTO);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userDTO");
        return "redirect:/";
    }

    @DeleteMapping("/withdraw")
    public String linkOut(@SessionAttribute("userDTO") ResponseUserDTO userDTO,
                          HttpSession session) {
        //logout
        String accessToken = oauthMap.get(this.oauthType).getAccessTokenByRefreshToken(userDTO.getRefreshToken());
        userService.deleteUser(userDTO.getId());
        oauthMap.get(this.oauthType).unlinkUserInfo(accessToken);
        session.removeAttribute("userDTO");
        return "redirect:/";
    }
}