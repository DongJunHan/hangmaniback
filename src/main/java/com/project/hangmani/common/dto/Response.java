package com.project.hangmani.common.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private int status;
    private String message;
    private T data;
}
