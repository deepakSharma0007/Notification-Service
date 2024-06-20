package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.controller;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.controller.BlacklistController;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.BlacklistNumber;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.requestbody.BlacklistedNumberRequestBody;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service.BlacklistNumberService;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service.RedisSetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(BlacklistController.class)
public class BlacklistControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RedisSetService redisSetService;
    @MockBean
    BlacklistNumberService blacklistNumberService;

    @Test
    public void testAddBlacklistedNumbers() throws Exception {
        // Mock request body
        List<String> phoneNumbers = Arrays.asList("+919234561789", "+917876543214");
        BlacklistedNumberRequestBody requestBody = new BlacklistedNumberRequestBody(phoneNumbers);

        // Serialize the request body to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        // Mock the behavior of blacklistNumberService.findByNumber() to return empty Optional
        when(blacklistNumberService.findByNumber(anyString())).thenReturn(Optional.empty());

        // Mock the behavior of blacklistNumberService.addBlacklistedNumber()
        doNothing().when(blacklistNumberService).addBlacklistedNumber(any(BlacklistNumber.class));

        // Mock the behavior of redisSetService.addToSet()
        doNothing().when(redisSetService).add(anyString());


        RequestBuilder request = MockMvcRequestBuilders
                        .post("/v1/blacklist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson)
                        .accept(MediaType.APPLICATION_JSON);

        // Perform the request and verify the response
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully blacklisted"));

        // Verify the interactions
        verify(blacklistNumberService, times(2)).findByNumber(anyString());
        verify(blacklistNumberService, times(2)).addBlacklistedNumber(any(BlacklistNumber.class));
        verify(redisSetService, times(2)).add(anyString());
    }

    @Test
    public void testAddBlacklistedNumbers_invalidNumber() throws Exception {
        // Mock request body
        List<String> phoneNumbers = Arrays.asList("+919234561789", "+9178ab765434");
        BlacklistedNumberRequestBody requestBody = new BlacklistedNumberRequestBody(phoneNumbers);

        // Serialize the request body to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        // Mock the behavior of blacklistNumberService.findByNumber() to return empty Optional
        when(blacklistNumberService.findByNumber(anyString())).thenReturn(Optional.empty());

        // Mock the behavior of blacklistNumberService.addBlacklistedNumber()
        doNothing().when(blacklistNumberService).addBlacklistedNumber(any(BlacklistNumber.class));

        // Mock the behavior of redisSetService.addToSet()
        doNothing().when(redisSetService).add(anyString());



        RequestBuilder request = MockMvcRequestBuilders
                .post("/v1/blacklist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .accept(MediaType.APPLICATION_JSON);

        // Perform the request and verify the response
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"code\":\"INVALID_REQUEST\",\"message\":\"+9178ab765434 is not a valid phone number\"}"));

        // Verify the interactions
        verify(blacklistNumberService, times(0)).findByNumber(anyString());
        verify(blacklistNumberService, times(0)).addBlacklistedNumber(any(BlacklistNumber.class));
        verify(redisSetService, times(0)).add(anyString());
    }

    @Test
    public void testRemoveBlacklistedNumbers() throws Exception {
        List<String> phoneNumbers = Arrays.asList("+917323456789", "+919087654321");
        BlacklistedNumberRequestBody requestBody = new BlacklistedNumberRequestBody(phoneNumbers);

        // Serialize the request body to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        // Mock the behavior of blacklistNumberService.findByNumber()
        when(blacklistNumberService.findByNumber(anyString())).thenReturn(Optional.of(new BlacklistNumber()));

        RequestBuilder request = MockMvcRequestBuilders
                .delete("/v1/blacklist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .accept(MediaType.APPLICATION_JSON);

        // Perform the request and verify the response
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully whitelisted"));

        // Verify the interactions
        verify(blacklistNumberService, times(4)).findByNumber(anyString());
        verify(blacklistNumberService, times(2)).deleteBlacklistedNumber(any(BlacklistNumber.class));
        verify(redisSetService, times(2)).remove(anyString());
    }

    @Test
    public void testRemoveBlacklistedNumbers_numberNotExist() throws Exception {
        List<String> phoneNumbers = Arrays.asList("+917323456789", "+919087654321");
        BlacklistedNumberRequestBody requestBody = new BlacklistedNumberRequestBody(phoneNumbers);

        // Serialize the request body to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        // Mock the behavior of blacklistNumberService.findByNumber()
        when(blacklistNumberService.findByNumber(anyString())).thenReturn(Optional.empty());

        RequestBuilder request = MockMvcRequestBuilders
                .delete("/v1/blacklist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .accept(MediaType.APPLICATION_JSON);

        // Perform the request and verify the response
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"code\":\"INVALID_REQUEST\",\"message\":\"+917323456789 not Blacklisted\"}"));

        // Verify the interactions
        verify(blacklistNumberService, times(1)).findByNumber(anyString());
        verify(blacklistNumberService, times(0)).deleteBlacklistedNumber(any(BlacklistNumber.class));
        verify(redisSetService, times(0)).remove(anyString());
    }

    @Test
    public void testRemoveBlacklistedNumbers_invalidNumber() throws Exception {
        List<String> phoneNumbers = Arrays.asList("+917323456789", "+919a087654321");
        BlacklistedNumberRequestBody requestBody = new BlacklistedNumberRequestBody(phoneNumbers);

        // Serialize the request body to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        RequestBuilder request = MockMvcRequestBuilders
                .delete("/v1/blacklist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
                .accept(MediaType.APPLICATION_JSON);

        // Perform the request and verify the response
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"code\":\"INVALID_REQUEST\",\"message\":\"+919a087654321 is not a valid phone number\"}"));

        // Verify the interactions
        verify(blacklistNumberService, times(0)).findByNumber(anyString());
        verify(blacklistNumberService, times(0)).deleteBlacklistedNumber(any(BlacklistNumber.class));
        verify(redisSetService, times(0)).remove(anyString());
    }

    @Test
    public void testGetBlacklistedNumbers() throws Exception {
        // Mock the behavior of redisSetService.getAllMembers()
        Set<String> blacklistedNumbersSet = new HashSet<>(Arrays.asList("+919123456789", "+919817654321"));
        when(redisSetService.findAll()).thenReturn(blacklistedNumbersSet);

        RequestBuilder request = MockMvcRequestBuilders.get("/v1/blacklist").contentType(MediaType.APPLICATION_JSON);


        // Perform the request and verify the response
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("[\"+919123456789\", \"+919817654321\"]"));

        // Verify the interaction
        verify(redisSetService, times(1)).findAll();
    }

}


