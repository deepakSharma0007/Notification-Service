package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.controller;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.SmsRequestElasticSearch;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.requestbody.SearchByPhoneNoRequestBody;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.requestbody.SearchByTextRequestBody;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service.SmsRequestElasticSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(SmsRequestElasticSearchController.class)
public class SmsRequestElasticSearchControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    SmsRequestElasticSearchService smsRequestElasticSearchService;

    @Test
    public void testGetSmsByPhoneNumberAndTimeRange() throws Exception {
        // Sample request body
        SearchByPhoneNoRequestBody requestBody = new SearchByPhoneNoRequestBody();
        requestBody.setPhoneNumber("+919234567890");
        requestBody.setStartTime("01-01-2023 00:00:00");
        requestBody.setEndTime("01-02-2023 00:00:00");
        requestBody.setPage(0);
        requestBody.setSize(10);

        // Serialize the request body to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        // Sample response
        List<SmsRequestElasticSearch> expectedResult = new ArrayList<>();
        String expectedResultJson = objectMapper.writeValueAsString(expectedResult);

        when(smsRequestElasticSearchService.findByPhoneNumberAndTimeRange(any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(expectedResult));

        RequestBuilder request = MockMvcRequestBuilders.get("/v1/sms/search/phone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .accept(MediaType.APPLICATION_JSON);

        // Perform the request and verify the response
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResultJson));

        verify(smsRequestElasticSearchService, times(1)).findByPhoneNumberAndTimeRange(any(), any(), any(), any());
    }

    @Test
    public void testGetSmsByText() throws Exception{
        // Sample request body
        SearchByTextRequestBody requestBody = new SearchByTextRequestBody();
        requestBody.setSearchText("Hello");
        requestBody.setPage(0);
        requestBody.setSize(20);


        // Serialize the request body to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        // Sample response
        List<SmsRequestElasticSearch> expectedResult = new ArrayList<>();
        String expectedResultJson = objectMapper.writeValueAsString(expectedResult);

        when(smsRequestElasticSearchService.findByTextContaining(any(), any())).thenReturn(new PageImpl<>(expectedResult));

        RequestBuilder request = MockMvcRequestBuilders.get("/v1/sms/search/text")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResultJson));

        verify(smsRequestElasticSearchService, times(1)).findByTextContaining(any(), any());
    }
}
