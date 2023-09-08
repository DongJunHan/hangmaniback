package com.project.hangmani.store.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestRangeDTO {
    @NotNull
    private Double startLatitude;
    @NotNull
    private Double endLatitude;
    @NotNull
    private Double startLongitude;
    @NotNull
    private Double endLongitude;
    private int limit;
    private int offset;
}
