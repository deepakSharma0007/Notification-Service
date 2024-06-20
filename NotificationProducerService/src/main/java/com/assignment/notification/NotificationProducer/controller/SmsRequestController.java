package com.assignment.notification.NotificationProducer.controller;

import com.assignment.notification.NotificationProducer.RequestBody.SmsRequestBody;
import com.assignment.notification.NotificationProducer.entity.SmsRequest;
import com.assignment.notification.NotificationProducer.exception.InvalidRequestException;
import com.assignment.notification.NotificationProducer.exception.NotFoundException;
import com.assignment.notification.NotificationProducer.kafka.MessageProducer;
import com.assignment.notification.NotificationProducer.service.SmsRequestService;
import com.assignment.notification.NotificationProducer.util.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static com.assignment.notification.NotificationProducer.util.PhoneNumberValidator.isValidPhoneNumber;



@Slf4j
@RestController
@RequestMapping("/v1/sms")
public class SmsRequestController {


//    kafka topic to which sms request id will be sent
    @Value("${kafka.topic}")
    String kafkaTopic;

    private final SmsRequestService smsRequestService;
    private  final MessageProducer messageProducer;

    @Autowired
    public SmsRequestController(SmsRequestService smsRequestService, MessageProducer messageProducer)
    {
        this.smsRequestService = smsRequestService;
        this.messageProducer = messageProducer;
    }

//    post api to send sms
    @PostMapping("/send")
    public SuccessResponse sendSmsRequest(@RequestBody SmsRequestBody smsRequestBody) {
        try{
            /* Checking phone number exist or not */
            if (smsRequestBody.getPhoneNumber() == null || smsRequestBody.getPhoneNumber().isEmpty()) {
                throw new InvalidRequestException("Phone no. is mandatory");
            }

            /*  Validating phone number */
            isValidPhoneNumber(smsRequestBody.getPhoneNumber());

            /* Creating a smsRequest entity to insert in "sms_requests" table */
            SmsRequest smsRequest = new SmsRequest();
            smsRequest.setRequestId(String.valueOf(UUID.randomUUID()));
            smsRequest.setPhoneNumber(smsRequestBody.getPhoneNumber());
            smsRequest.setMessage(smsRequestBody.getMessage());
            smsRequest.setStatus("Pending");
            smsRequest.setCreatedAt(Timestamp.from(Instant.now()));
            smsRequest.setUpdatedAt(Timestamp.from(Instant.now()));

            /* Saving smsRequest into database */
            String requestId = smsRequestService.AddMessageRequest(smsRequest);
            log.info("sms request is saved in database with request id: " + requestId);

            /* Sending the sms request_id to kafka */
            messageProducer.sendMessage(kafkaTopic, requestId);
            log.info("sms request id is sent to kafka");
            SuccessResponse successResponse = new SuccessResponse();
            successResponse.setRequestId(requestId);
            successResponse.setComments("Successfully sent");
            return successResponse;
        }
        catch (Exception e)
        {
            log.error("Error in send message api. Error Message : " + e.getMessage());
            throw  e;
        }
    }

    /* Get api to fetch message record by requestId */
    @GetMapping("/{requestId}")
    public ResponseEntity<?> getSmsRequestDetails(@PathVariable String requestId)
    {
        try{
            SmsRequest response = smsRequestService.findByRequestId(requestId).
                    orElseThrow(() -> new NotFoundException("request_id " + requestId + " not found"));
            log.info("Sending message record with request id:"+ requestId + " in response");
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            log.error("Error in get message by request id. Error Message : " + e.getMessage());
            throw e;
        }
    }
}
