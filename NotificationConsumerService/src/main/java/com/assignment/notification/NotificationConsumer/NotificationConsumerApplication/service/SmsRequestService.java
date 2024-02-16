package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.SmsRequest;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.repository.SmsRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SmsRequestService{
    private final SmsRequestRepository smsRequestRepository;

    @Autowired
    public SmsRequestService(SmsRequestRepository smsRequestRepository)
    {
        this.smsRequestRepository = smsRequestRepository;
    }

    public Optional<SmsRequest> findByRequestId(String requestId)
    {
        return smsRequestRepository.findByRequestId(requestId);
    }

    public SmsRequest update(SmsRequest smsRequest)
    {
        return smsRequestRepository.save(smsRequest);
    }
}
