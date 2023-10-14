package com.project.hangmani.store.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestDeleteDTO {
    @NotBlank
    private String storeUuid;
    @Builder
    private RequestDeleteDTO(String storeUuid) {
        this.storeUuid = storeUuid;
    }
}