package com.project.hangmani.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class DataResponseDTO<T> extends ResponseDTO{
    private final T data;
    private DataResponseDTO(T data) {
        super(HttpStatus.OK, "success");
        this.data = data;
    }
    private DataResponseDTO(T data, String message) {
        super(HttpStatus.OK, message);
        this.data = data;
    }
    public static <T> DataResponseDTO<T> of(T data) {
        return new DataResponseDTO<>(data);
    }
    public static <T> DataResponseDTO<T> of(T data, String message) {
        return new DataResponseDTO<>(data, message);
    }
    public static <T> DataResponseDTO<T> empty() {
        return new DataResponseDTO<>(null);
    }

}
