package com.assignment.notification.NotificationProducer.RequestBody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SmsRequestBody {
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String message;

    public SmsRequestBody()
    {}

    public SmsRequestBody(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }
}
