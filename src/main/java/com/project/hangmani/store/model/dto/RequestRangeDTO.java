package com.project.hangmani.store.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @Builder
    private RequestRangeDTO(Double startLatitude, Double endLatitude, Double startLongitude,
                            Double endLongitude, int limit, int offset) {
        this.startLatitude = startLatitude;
        this.endLatitude = endLatitude;
        this.startLongitude = startLongitude;
        this.endLongitude = endLongitude;
        this.limit = limit;
        this.offset = offset;
    }
}
