package com.project.hangmani.controller;

import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.oauth.service.OAuth;
import com.project.hangmani.oauth.service.KakaoOAuthService;
import com.project.hangmani.oauth.service.OAuthFactory;
import com.project.hangmani.user.model.dto.RequestInsertDTO;
import com.project.hangmani.user.model.dto.ResponseDTO;
import com.project.hangmani.user.service.UserService;
import com.project.hangmani.util.ConvertData;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.project.hangmani.config.OAuthConst.KAKAO_TYPE;


@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
//    private Map<String, OAuth> oauthMap;
    private UserService userService;
    private ConvertData convertData;
    private String oauthType;
    private OAuthFactory oAuthFactory;
    private static Map<String, OAuth> userMap = new ConcurrentHashMap<>();

    public UserController(UserService userService, PropertiesValues propertiesValues) {
        this.userService = userService;
        this.convertData = new ConvertData(propertiesValues);
//        oauthMap.put(KAKAO_TYPE, oAuthService);
//        OAuth instance = oAuthFactory.getInstance(oAuthService.getType());
    }
    @GetMapping("/{oauth_type}")
    public String login(@PathVariable("oauth_type") String oauth_type) {
        this.oauthType = oauth_type;
        return "redirect:" + oAuthFactory.getInstance(oauth_type).getAuthorizationUrl();
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
        Map<String, Object> respTable = oAuthFactory.getInstance(this.oauthType).getAccessTokenByCode(code);
        Map<String, Object> kakaoUserInfo = oAuthFactory.getInstance(this.oauthType).getUserInfo(respTable);

        RequestInsertDTO userDTO = new RequestInsertDTO(convertData).convertToDTO(respTable, kakaoUserInfo, this.oauthType);
        ResponseDTO responseUserDTO = userService.InsertUser(userDTO);

        session.setAttribute("userDTO", responseUserDTO);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userDTO");
        return "redirect:/";
    }

    @DeleteMapping("/withdraw")
    public String linkOut(@SessionAttribute("userDTO") ResponseDTO userDTO,
                          HttpSession session) {
        //logout
        String accessToken = oAuthFactory.getInstance(this.oauthType).getAccessTokenByRefreshToken(userDTO.getRefreshToken());
        userService.deleteUser(userDTO.getId());
        oAuthFactory.getInstance(this.oauthType).unlinkUserInfo(accessToken);
        session.removeAttribute("userDTO");
        return "redirect:/";
    }
}