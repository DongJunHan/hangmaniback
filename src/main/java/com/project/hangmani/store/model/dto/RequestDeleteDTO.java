package com.project.hangmani.store.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestDeleteDTO {
    @NotBlank
    private String storeUuid;
}