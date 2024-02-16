package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.util;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.SmsRequest;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.SmsRequestElasticSearch;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.requestbody.SmsRequestBody;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service.RedisSetService;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service.SmsRequestElasticSearchService;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service.SmsRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/* This is sms gateway class which contains methods to create a json body and make a call to the third party api */
@Slf4j
@Component
public class SmsGateway {

    @Value("${thirdparty.apiUrl}")
    private String apiUrl;
//    private final String apiUrl = "https://api.imiconnect.in/resources/v1/messaging";

    @Value("${thirdparty.apiKeyBody}")
    private String apiKey;
//    private final String apiKey = "c0c49ebf-ca44-11e9-9e4e-025282c394f2";

    @Value("${thirdparty.apiKeyHeader}")
    private String apiKeyHeader;
//    private final String apiKeyHeader = "key";

    private final SmsRequestService smsRequestService;
    private final SmsRequestElasticSearchService smsRequestElasticSearchService;

    private final RedisSetService redisSetService;


    @Autowired
    public SmsGateway(SmsRequestService smsRequestService, SmsRequestElasticSearchService smsRequestElasticSearchService, RedisSetService redisSetService)
    {
        this.smsRequestService = smsRequestService;
        this.smsRequestElasticSearchService = smsRequestElasticSearchService;
        this.redisSetService = redisSetService;
    }

    public void handleResponse(SmsRequest smsRequest, String status, String failureComment, int failureCode)
    {
        smsRequest.setStatus(status);
        smsRequest.setFailureComments(failureComment);
        smsRequest.setFailureCode(failureCode);
        smsRequest.setUpdatedAt(Timestamp.from(Instant.now()));
        SmsRequest updateSmsRequest = smsRequestService.update(smsRequest);
        SmsRequestElasticSearch smsRequestElasticSearch = getSmsRequestElasticSearch(updateSmsRequest);
        smsRequestElasticSearchService.save(smsRequestElasticSearch);
    }

    public void sendSms(String requestId)
    {
        Optional<SmsRequest> smsRequestRecord = smsRequestService.findByRequestId(requestId);

        if(smsRequestRecord.isPresent())
        {
            /* setting up all the fields required to make a call to third party API */
            SmsRequest smsRequest = smsRequestRecord.get();

            /* To handle to downtime of redis */
            boolean isBlacklisted = false;
            try{
                isBlacklisted = redisSetService.isExists(smsRequest.getPhoneNumber());
            }
            catch (Exception e)
            {
                log.error("Failed to send SMS. Status code: " + HttpStatus.INTERNAL_SERVER_ERROR);
                log.error("Redis is down. Error Message: " + e.getMessage());
                handleResponse(smsRequest, "Failed", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
                log.info("Sms record is updated in database and saved in elastic search");
                return;
            }

            /* checking whether number is blacklist or not */
            if(isBlacklisted)
            {
                log.error("Failed to send SMS. Status code: " + HttpStatus.FORBIDDEN);
                log.error("Response: Message delivery failed: The recipient's phone number, " + smsRequest.getPhoneNumber() + ", has been flagged for blacklisting.");
                smsRequest.setStatus("Failed");
                String failureComment = "Message delivery failed: The recipient's phone number, " + smsRequest.getPhoneNumber() + ", has been flagged for blacklisting.";
                handleResponse(smsRequest, "Failed", failureComment, HttpStatus.FORBIDDEN.value());
                log.info("Sms record is updated in database and saved in elastic search");
            }
            else
            {
                String deliveryChannel = "sms";
                String text = smsRequest.getMessage();
                List<String> msisdn = new ArrayList<String>();
                msisdn.add(smsRequest.getPhoneNumber());
                String correlationId = UUID.randomUUID().toString();

                SmsRequestBody smsRequestBody = new SmsRequestBody(deliveryChannel, text, msisdn, correlationId);
                log.info("Sms request body is build for passing to third party API");

                /* Create a RestTemplate instance */
                RestTemplate restTemplate = new RestTemplate();

                /* Set up headers */
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set(apiKeyHeader, apiKey);

                /* Convert the SmsRequest object to JSON */
                String jsonPayload;
                try {
                    jsonPayload = new ObjectMapper().writeValueAsString(smsRequestBody);
                } catch (JsonProcessingException e) {
                    log.error("Error converting SmsRequest to JSON: " + e.getMessage());
                    return;
                }

                log.info("JsonPayload: " + jsonPayload);

                /* Create the HttpEntity with headers and payload */
                HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

                /* Make the POST request to third party api */
                try{
                    ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
                    if (responseEntity.getStatusCode() == HttpStatus.OK) {
                        smsRequest.setStatus("Success");
                        log.info("SMS sent successfully!");
                        log.info("Response: " + responseEntity.getBody());
                    } else {
                        smsRequest.setStatus("Failed");
                        smsRequest.setFailureCode(responseEntity.getStatusCode().value());
                        smsRequest.setFailureComments(responseEntity.getBody());
                        log.error("Failed to send SMS. Status code: " + responseEntity.getStatusCode());
                        log.error("Response: " + responseEntity.getBody());
                    }
                }
                catch (Exception e)
                {
                    smsRequest.setStatus("Failed");
                    smsRequest.setFailureCode(HttpStatus.BAD_GATEWAY.value());
                    smsRequest.setFailureComments(e.getMessage());
                    log.error("Failed to send SMS. Status code: " + HttpStatus.BAD_GATEWAY.value());
                    log.error("Response: " + e.getMessage());
                }
                finally {
                    /* updating updatedAt field of sms record in database */
                    smsRequest.setUpdatedAt(Timestamp.from(Instant.now()));

                    SmsRequest updateSmsRequest = smsRequestService.update(smsRequest);
                    SmsRequestElasticSearch smsRequestElasticSearch = getSmsRequestElasticSearch(updateSmsRequest);
                    smsRequestElasticSearchService.save(smsRequestElasticSearch);
                    log.info("Sms record is updated in database and saved in elastic search");
                }
            }
        }

    }

    /* returns an object to save in elastic search */
    private static SmsRequestElasticSearch getSmsRequestElasticSearch(SmsRequest updateSmsRequest) {
        SmsRequestElasticSearch smsRequestElasticSearch = new SmsRequestElasticSearch();
        smsRequestElasticSearch.setId(updateSmsRequest.getId());
        smsRequestElasticSearch.setRequestId(updateSmsRequest.getRequestId());
        smsRequestElasticSearch.setPhoneNumber(updateSmsRequest.getPhoneNumber());
        smsRequestElasticSearch.setMessage(updateSmsRequest.getMessage());
        smsRequestElasticSearch.setStatus(updateSmsRequest.getStatus());
        smsRequestElasticSearch.setFailureCode(updateSmsRequest.getFailureCode());
        smsRequestElasticSearch.setFailureComments(updateSmsRequest.getFailureComments());
        smsRequestElasticSearch.setCreatedAt(updateSmsRequest.getCreatedAt());
        smsRequestElasticSearch.setUpdatedAt(updateSmsRequest.getUpdatedAt());
        return smsRequestElasticSearch;
    }
}
