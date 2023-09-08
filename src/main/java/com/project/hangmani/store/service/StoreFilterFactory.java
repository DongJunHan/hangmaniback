package com.project.hangmani.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StoreFilterFactory {
    private final List<StoreFilter> list;

    public StoreFilter getInstance(String type) {
        return list.stream()
                .filter(t -> t.getType().equals(type))
                .findAny()
                .orElse(null);
    }
}
