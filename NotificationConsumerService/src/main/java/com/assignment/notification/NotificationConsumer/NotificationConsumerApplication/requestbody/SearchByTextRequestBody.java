package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchByTextRequestBody {
    @JsonProperty("search_text")
    private String searchText;
    private int page = 0;
    private int size = 10;
}
