package com.assignment.notification.NotificationProducer.exception;

import com.assignment.notification.NotificationProducer.util.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(InvalidRequestException exc)
    {
        System.out.println(exc.getMessage());
        ExceptionResponse error = new ExceptionResponse();
        error.setCode("INVALID_REQUEST");
        error.setMessage(exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(NotFoundException exc)
    {
        ExceptionResponse error = new ExceptionResponse();
        error.setCode("NOT_FOUND");
        error.setMessage(exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    public ResponseEntity<ExceptionResponse> handleException(Exception exc)
    {
        System.out.println(exc.getMessage());
        ExceptionResponse error = new ExceptionResponse();
        error.setCode("UNKNOWN_ERROR");
        error.setMessage(exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
