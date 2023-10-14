package com.project.hangmani.common.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Response<T> {
    private int status;
    private String message;
    private T data;
    @Builder
    private Response(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
