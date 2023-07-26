package com.example.reactive.exception.handler;

import com.example.reactive.exception.InternalServerException;
import com.example.reactive.exception.InvalidRequestException;
import com.example.reactive.exception.NoSuchElementFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.server.EntityResponse;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {
    @ExceptionHandler(NoSuchElementFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public EntityResponse<ErrorMessage> resourceNotFoundException(NoSuchElementFoundException ex) {
            return EntityResponse
                .fromObject(ErrorMessage.builder()
                        .timestamp(ZonedDateTime.now())
                        .status(HttpStatus.NOT_FOUND.value())
                        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .message(ex.getMessage())
                        .build())
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .build().block();
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EntityResponse<ErrorMessage> invalidRequestException(InvalidRequestException ex) {
        return EntityResponse
                .fromObject(ErrorMessage.builder()
                        .timestamp(ZonedDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message(ex.getMessage())
                        .build())
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .build().block();
    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public EntityResponse<ErrorMessage> internalServerException(InternalServerException ex) {
        return EntityResponse
                .fromObject(ErrorMessage.builder()
                        .timestamp(ZonedDateTime.now())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .message("It was not possible to process the request.")
                        .build())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .build().block();
    }
}
