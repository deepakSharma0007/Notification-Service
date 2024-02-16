package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.BlacklistNumber;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.repository.BlacklistNumberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlacklistNumberServiceTest {
    @Mock
    BlacklistNumberRepository blacklistNumberRepository;
    @InjectMocks
    BlacklistNumberService blacklistNumberService;

    @Test
    public void testGetAllNumbers()
    {
        // Create a list of BlacklistNumber objects for testing
        List<BlacklistNumber> expectedNumbers = Arrays.asList(
                new BlacklistNumber(1, "+919123456789"),
                new BlacklistNumber(2, "+919187654321")
        );

        // Mock the behavior of blacklistNumberRepository.findAll()
        when(blacklistNumberRepository.findAll()).thenReturn(expectedNumbers);

        // Call the method under test
        List<BlacklistNumber> actualNumbers = blacklistNumberService.getAllNumbers();

        // Verify that the method returns the expected list of BlacklistNumber objects
        assertEquals(expectedNumbers.size(), actualNumbers.size());
        for (int i = 0; i < expectedNumbers.size(); i++) {
            assertEquals(expectedNumbers.get(i).getPhoneNumber(), actualNumbers.get(i).getPhoneNumber());
        }

        // Verify that blacklistNumberRepository.findAll() is called once
        verify(blacklistNumberRepository, times(1)).findAll();
    }

    @Test
    public void testAddBlacklistedNumber() {
        // Create a BlacklistNumber object for testing
        BlacklistNumber blacklistNumber = new BlacklistNumber(1, "+919232345689");

        // Call the method under test
        blacklistNumberService.addBlacklistedNumber(blacklistNumber);

        // Verify that blacklistNumberRepository.save() is called with the expected BlacklistNumber object
        verify(blacklistNumberRepository, times(1)).save(blacklistNumber);
    }

    @Test
    public void testFindByNumber() {
        // Create a phone number for testing
        String phoneNumber1 = "+919212345678";
        String phoneNumber2 = "+916767547867";

        // Create a BlacklistNumber object for testing
        BlacklistNumber blacklistNumber1 = new BlacklistNumber(1, phoneNumber1);
        BlacklistNumber blacklistNumber2 = new BlacklistNumber(2, phoneNumber2);

        // Mock the behavior of blacklistNumberRepository.findById()
        when(blacklistNumberRepository.findByPhoneNumber(phoneNumber1)).thenReturn(Optional.of(blacklistNumber1));
        when(blacklistNumberRepository.findByPhoneNumber(phoneNumber2)).thenReturn(Optional.empty());

        // Call the method under test
        Optional<BlacklistNumber> result1 = blacklistNumberService.findByNumber(phoneNumber1);
        Optional<BlacklistNumber> result2 = blacklistNumberService.findByNumber(phoneNumber2);

        // Verify that blacklistNumberRepository.findById() is called with the expected phone number
        verify(blacklistNumberRepository, times(1)).findByPhoneNumber(phoneNumber1);
        verify(blacklistNumberRepository, times(1)).findByPhoneNumber(phoneNumber2);

        // Verify that the method returns the expected result
        assertTrue(result1.isPresent());
        assertEquals(blacklistNumber1, result1.get());
        assertTrue(result2.isEmpty());
    }

    @Test
    public void testDeleteBlacklistedNumber() {
        // Create a BlacklistNumber object for testing
        BlacklistNumber blacklistNumber = new BlacklistNumber(1, "123456789");

        // Call the method under test
        blacklistNumberService.deleteBlacklistedNumber(blacklistNumber);

        // Verify that blacklistNumberRepository.delete() is called with the expected BlacklistNumber object
        verify(blacklistNumberRepository, times(1)).delete(blacklistNumber);
    }


}
