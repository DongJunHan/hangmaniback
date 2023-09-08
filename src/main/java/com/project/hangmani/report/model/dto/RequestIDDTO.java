package com.project.hangmani.report.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestIDDTO {
    @NotBlank
    private String id;
}
