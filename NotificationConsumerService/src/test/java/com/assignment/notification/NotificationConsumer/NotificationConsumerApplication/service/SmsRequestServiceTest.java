package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.SmsRequest;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.repository.SmsRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SmsRequestServiceTest {
    @Mock
    SmsRequestRepository smsRequestRepository;

    @InjectMocks
    SmsRequestService smsRequestService;


    @Test
    public void testFindByRequestId_idExists()
    {
        // Create a sample SmsRequest object
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setId(1);
        smsRequest.setRequestId("abc");
        smsRequest.setPhoneNumber("+919878673435");
        smsRequest.setMessage("Hello from other side");
        smsRequest.setStatus("Pending");
        smsRequest.setCreatedAt(Timestamp.valueOf("2024-01-31 07:08:43"));
        smsRequest.setUpdatedAt(Timestamp.valueOf("2024-01-31 07:08:43"));

        // Mock the behavior of smsRequestRepository.findById() to return the sample SmsRequest object
        when(smsRequestRepository.findByRequestId("abc")).thenReturn(Optional.of(smsRequest));

        // Call the method under test
        Optional<SmsRequest> result = smsRequestService.findByRequestId("abc");

        // Verify that smsRequestRepository.findById() is called with the correct requestId
        verify(smsRequestRepository, times(1)).findByRequestId("abc");

        // Assert that the returned Optional contains the correct SmsRequest object
        assertTrue(result.isPresent());
        assertEquals(smsRequest, result.get());
    }

    @Test
    public void testFindByRequestId_idNotExists()
    {
        // Mock the behavior of smsRequestRepository.findById() to return an empty Optional
        when(smsRequestRepository.findByRequestId("abc")).thenReturn(Optional.empty());

        // Call the method under test
        Optional<SmsRequest> result = smsRequestService.findByRequestId("abc");

        // Verify that smsRequestRepository.findById() is called with the correct requestId
        verify(smsRequestRepository, times(1)).findByRequestId("abc");

        // Assert that the returned Optional is empty
        assertFalse(result.isPresent());
    }

    @Test
    public void testUpdate() {
        // Create a SmsRequest object for testing
        SmsRequest expectedResult = new SmsRequest();
        expectedResult.setId(1); // Set the ID to identify the object
        expectedResult.setRequestId("abc");
        expectedResult.setPhoneNumber("+919213456789");
        expectedResult.setMessage("Hello World");
        expectedResult.setFailureComments(null);
        expectedResult.setUpdatedAt(Timestamp.from(Instant.now()));
        expectedResult.setCreatedAt(Timestamp.from(Instant.now()));
        expectedResult.setFailureCode(0);
        expectedResult.setStatus("success");

        // Mock the behavior of smsRequestRepository.save()
        when(smsRequestRepository.save(expectedResult)).thenReturn(expectedResult);

        // Call the method under test
        SmsRequest actualResult = smsRequestService.update(expectedResult);

        // Verify that smsRequestRepository.save() is called with the expected SmsRequest object
        verify(smsRequestRepository, times(1)).save(expectedResult);

        // Verify that the method returns the expected SmsRequest object
        assertEquals(expectedResult, actualResult);
    }

}