package com.assignment.notification.NotificationProducer.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public MessageProducer(KafkaTemplate<String, String> kafkaTemplate)
    {
        this.kafkaTemplate = kafkaTemplate;
    }

    /* Method to send message to kafka topic*/
    public void sendMessage(String topic, String message)
    {
        log.info("Sending a message: " + message + " to kafka topic: " + topic);
        kafkaTemplate.send(topic, message);
    }
}
