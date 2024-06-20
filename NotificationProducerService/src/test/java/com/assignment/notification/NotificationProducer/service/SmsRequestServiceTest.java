package com.assignment.notification.NotificationProducer.service;

import com.assignment.notification.NotificationProducer.entity.SmsRequest;
import com.assignment.notification.NotificationProducer.repository.SmsRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
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
    public void testAddMessageRequest()
    {
        // Create a sample SmsRequest object
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setRequestId("abc"); // Assuming you set the ID in the service method
        smsRequest.setPhoneNumber("+919878673435");
        smsRequest.setMessage("Hello from other side");
        smsRequest.setStatus("Pending");
        smsRequest.setCreatedAt(Timestamp.valueOf("2024-01-31 07:08:43"));
        smsRequest.setUpdatedAt(Timestamp.valueOf("2024-01-31 07:08:43"));

        // Mock the behavior of smsRequestRepository.save() to return the same SmsRequest object
        when(smsRequestRepository.save(any(SmsRequest.class))).thenReturn(smsRequest);

        // Call the method under test
        String requestId = smsRequestService.AddMessageRequest(smsRequest);

        // Verify that smsRequestRepository.save() is called with the correct SmsRequest object
        verify(smsRequestRepository, times(1)).save(smsRequest);

        // Assert that the returned requestId is equal to the ID of the SmsRequest object
        assertEquals(smsRequest.getRequestId(), requestId);
    }

    @Test
    public void testFindByRequestId_idExists()
    {
        // Create a sample SmsRequest object
        SmsRequest smsRequest = new SmsRequest();
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
}
