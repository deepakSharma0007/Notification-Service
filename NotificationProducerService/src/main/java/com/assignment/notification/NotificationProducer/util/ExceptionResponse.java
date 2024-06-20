package com.assignment.notification.NotificationProducer.util;
import lombok.Data;

/* This is exception response template which we will return whenever some exception occurs */
@Data
public class ExceptionResponse {
    private String code;
    private String message;

    public ExceptionResponse()
    {
    }
}

