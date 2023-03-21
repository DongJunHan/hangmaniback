package com.project.hangmani.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class BasicController {

    @GetMapping("/basic/login")
    public String afterLogin() {
        return "basic/login";
    }
}
