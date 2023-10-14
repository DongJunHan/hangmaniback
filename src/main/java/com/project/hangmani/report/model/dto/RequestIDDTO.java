package com.project.hangmani.report.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestIDDTO {
    @NotBlank
    private String id;
    @Builder
    private RequestIDDTO(String id) {
        this.id = id;
    }
}
