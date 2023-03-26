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
            this.oauth_type = oauth_type;
            return "redirect:" + oAuthService.getAuthorizationUrl(KAUTH_HOST, KAKAO_SCOPE);

//        }else if() {
//            this.oAuthService = googleOAuthService;
//            this.oauth_type = oauth_type;

        }
        else{
            log.error("we are get");
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
        Map<String, Object> respTable = oAuthService.getAccessTokenByCode(code);
        Map<String, Object> kakaoUserInfo = oAuthService.getUserInfo(respTable);

        RequestInsertUserDTO userDTO = requestConvert.convertDTO(respTable, kakaoUserInfo, this.oauth_type);
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
        log.info("refresh_token={}", userDTO.getRefreshToken());
        //logout
        String accessToken = oAuthService.getAccessTokenByRefreshToken(userDTO.getRefreshToken());
        userService.deleteUser(userDTO.getId());
        oAuthService.unlinkUserInfo(accessToken);
        session.removeAttribute("userDTO");
        return "redirect:/";
    }
}