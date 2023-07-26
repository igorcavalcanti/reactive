package com.example.reactive.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
public class ErrorMessage {
    private ZonedDateTime timestamp;
    @Schema(description = "HTTP Status code")
    private Integer status;
    @Schema(description = "HTTP code error")
    private String error;
    @Schema(description = "Actual error message")
    private String message;
}
