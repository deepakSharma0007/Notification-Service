package com.assignment.notification.NotificationProducer.controller;

import com.assignment.notification.NotificationProducer.RequestBody.SmsRequestBody;
import com.assignment.notification.NotificationProducer.entity.SmsRequest;
import com.assignment.notification.NotificationProducer.exception.NotFoundException;
import com.assignment.notification.NotificationProducer.kafka.MessageProducer;
import com.assignment.notification.NotificationProducer.service.SmsRequestService;
import com.assignment.notification.NotificationProducer.util.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(SmsRequestController.class)
public class SmsRequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SmsRequestService smsRequestService;

    @MockBean
    MessageProducer messageProducer;

    @Test
    public void testSendSmsRequest_invalidPhoneNumber() throws Exception {
        SmsRequestBody smsRequestBody = new SmsRequestBody("+9198233abc4532", "Hello World");

        // Serialize the request body to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(smsRequestBody);

        // Expected response
        String expectedResponseBody = "{\"code\":\"INVALID_REQUEST\",\"message\":\"+9198233abc4532 is not a valid phone number\"}";


        // Create the request
        RequestBuilder request = MockMvcRequestBuilders
                .post("/v1/sms/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .accept(MediaType.APPLICATION_JSON);

        // Perform the request and assert the response
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())  // Assuming you expect a 200 OK status
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    public void testSendSmsRequest_withoutPhoneNumber() throws Exception {
        SmsRequestBody smsRequestBody = new SmsRequestBody();
        smsRequestBody.setMessage("Hello World");

        // Serialize the request body to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(smsRequestBody);

        // Expected response
        String expectedResponseBody = "{\"code\":\"INVALID_REQUEST\",\"message\":\"Phone no. is mandatory\"}";


        // Create the request
        RequestBuilder request = MockMvcRequestBuilders
                .post("/v1/sms/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .accept(MediaType.APPLICATION_JSON);

        // Perform the request and assert the response
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())  // Assuming you expect a 200 OK status
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    public void testSendSmsRequest_emptyPhoneNumber() throws Exception {
        SmsRequestBody smsRequestBody = new SmsRequestBody("", "Hello World");

        // Serialize the request body to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(smsRequestBody);

        // Expected response
        String expectedResponseBody = "{\"code\":\"INVALID_REQUEST\",\"message\":\"Phone no. is mandatory\"}";


        // Create the request
        RequestBuilder request = MockMvcRequestBuilders
                .post("/v1/sms/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .accept(MediaType.APPLICATION_JSON);

        // Perform the request and assert the response
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())  // Assuming you expect a 200 OK status
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    public void testSendSmsRequest_validData() throws Exception {
        SmsRequestBody smsRequestBody = new SmsRequestBody("+919878786765", "Hello World");

        // Serialize the request body to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(smsRequestBody);

        // Expected response
        String expectedResponseBody = "{request_id: \"abc\", comments:\"Successfully sent\"}";

        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setRequestId("abc");
        smsRequest.setId(1);
        smsRequest.setPhoneNumber(smsRequestBody.getPhoneNumber());
        smsRequest.setMessage(smsRequestBody.getMessage());
        smsRequest.setStatus("Pending");
        smsRequest.setCreatedAt(Timestamp.from(Instant.now()));
        smsRequest.setUpdatedAt(Timestamp.from(Instant.now()));

        when(smsRequestService.AddMessageRequest(ArgumentMatchers.any(SmsRequest.class))).thenReturn("abc");

        // Create the request
        RequestBuilder request = MockMvcRequestBuilders
                .post("/v1/sms/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .accept(MediaType.APPLICATION_JSON);


        // Perform the request and assert the response
        mockMvc.perform(request)
                .andExpect(status().isOk())  // Assuming you expect a 200 OK status
                .andExpect(content().json(expectedResponseBody));

    }


    @Test
    public void testGetSmsRequestDetails_validRequestId() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/v1/sms/abc");

        // Expected response
        String expectedResponseBody = "{\"id\":1,\"message\":\"Hello from other side\",\"status\":\"Pending\",\"request_id\":\"abc\",\"phone_number\":\"+919878673435\",\"failure_code\":0,\"failure_comments\":null,\"created_at\":\"2024-01-31T01:38:43.000+00:00\",\"updated_at\":\"2024-01-31T01:38:43.000+00:00\"}";

        SmsRequest smsRequest = new SmsRequest(1, "abc", "+919878673435", "Hello from other side", "Pending", 0, null, Timestamp.valueOf("2024-01-31 07:08:43"), Timestamp.valueOf("2024-01-31 07:08:43"));


        when(smsRequestService.findByRequestId("abc")).thenReturn(Optional.of(smsRequest));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    public void testGetSmsRequestDetails_InvalidRequestId() throws Exception{
        RequestBuilder request = MockMvcRequestBuilders.get("/v1/sms/-1");
        // Expected response
        String expectedResponseBody = "{code:\"NOT_FOUND\",message:\"request_id -1 not found\"}";

        when(smsRequestService.findByRequestId("-1")).thenThrow(new NotFoundException("request_id -1 not found"));

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedResponseBody));
    }



}
