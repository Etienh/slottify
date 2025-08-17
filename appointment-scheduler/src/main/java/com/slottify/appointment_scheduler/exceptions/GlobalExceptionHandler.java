package com.slottify.appointment_scheduler.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final String GENERIC_MESSAGE = "Unexpected error occurred, try again later or contact the Administrator";

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleNotCatchException(Exception e){
        log.error(GENERIC_MESSAGE);
        log.error(e.getMessage());
        return new ResponseEntity<>(GENERIC_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
