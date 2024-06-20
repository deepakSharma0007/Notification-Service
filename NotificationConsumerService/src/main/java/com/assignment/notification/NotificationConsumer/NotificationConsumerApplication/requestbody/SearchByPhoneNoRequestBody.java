package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class SearchByPhoneNoRequestBody {
    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("start_time")
//    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
//    private LocalDateTime startTime;
    private String startTime;

    @JsonProperty("end_time")
//    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
//    private LocalDateTime endTime;
    private String endTime;

    private int page = 0;
    private int size = 10;
}
