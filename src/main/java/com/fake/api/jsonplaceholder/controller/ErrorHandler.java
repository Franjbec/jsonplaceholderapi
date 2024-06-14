package com.fake.api.jsonplaceholder.controller;

import com.fake.api.jsonplaceholder.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @AllArgsConstructor
    @Getter
    private class ErrorMessage {
        private String msg;
    }

    @ExceptionHandler(value = { NotFoundException.class })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessage onNotFoundException(RuntimeException ex, WebRequest request) {

        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage unexpectedException(RuntimeException ex, WebRequest request) {
        log.error("Error inesperado: {}",ex.getLocalizedMessage(),ex);
        return new ErrorMessage(String.format("Error inesperado: %s", ex.getLocalizedMessage()));
    }
}
