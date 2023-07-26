package com.example.reactive.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchWorkerShiftDTO {
    @Schema(description = "Facility's identification")
    private Long facilityId;

    @Schema(description = "Shifts' identification")
    private String[] shifts;
    @Schema(description = "The lower bound of the Shift")
    private ZonedDateTime start;
    @Schema(description = "The upper bound of the Shift")
    private ZonedDateTime end;
}
