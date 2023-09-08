package com.project.hangmani.oauth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuthFactory {
    private final List<OAuth> list;

    public OAuth getInstance(String type) {
        return list.stream()
                .filter(t -> t.getType().equals(type))
                .findAny()
                .orElse(null);
    }
}
