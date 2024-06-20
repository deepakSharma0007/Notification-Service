package com.assignment.notification.NotificationProducer.service;

import com.assignment.notification.NotificationProducer.entity.SmsRequest;
import com.assignment.notification.NotificationProducer.repository.SmsRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SmsRequestService {
    private final SmsRequestRepository smsRequestRepository;

    @Autowired
    public SmsRequestService(SmsRequestRepository smsRequestRepository)
    {
        this.smsRequestRepository = smsRequestRepository;
    }

    /* Add new sms record to database and return request id*/
    public String AddMessageRequest(SmsRequest smsRequest)
    {
        smsRequestRepository.save(smsRequest);
        return smsRequest.getRequestId();
    }

    /* return sms record with given request id if it exist */
    public Optional<SmsRequest> findByRequestId(String requestId)
    {
        return smsRequestRepository.findByRequestId(requestId);
    }
}
