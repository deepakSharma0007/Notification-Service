package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlacklistedNumberRequestBody {
    @JsonProperty("phone_numbers")
    private List<String> phoneNumbers;
}

