package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.kafka;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.SmsRequest;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service.SmsRequestService;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.util.SmsGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class MessageConsumer {

    private final SmsGateway smsGateway;
    @Autowired
    public MessageConsumer(SmsGateway smsGateway)
    {
        this.smsGateway = smsGateway;
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String requestId) {
        log.info("Message received from kafka. Message: " + requestId);
        smsGateway.sendSms(requestId);
    }
}
