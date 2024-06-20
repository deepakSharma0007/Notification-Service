package com.assignment.notification.NotificationProducer.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/* This is success response template*/
@Data
public class SuccessResponse {
    @JsonProperty("request_id")
    private String requestId;
    private String comments;
}
