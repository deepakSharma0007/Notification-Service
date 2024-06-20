package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.config.RedisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RedisSetServiceTest {
    @Mock
    Jedis jedis;

    @InjectMocks
    RedisSetService redisSetService;

    @Test
    public void testAddToSet() {
        // Mock the behavior of jedis.set()
        String phoneNo = "+919834567892";
        when(jedis.set(phoneNo, "1")).thenReturn("1");

        // Call the method under test
        redisSetService.add(phoneNo);

        // Verify that jedis.set() is called with the correct parameters
        verify(jedis, times(1)).set(phoneNo, "1");
    }

    @Test
    public void testRemoveFromSet() {
        // Mock the behavior of jedis.del()
        String setName = "blacklistNumbers";
        String phoneNo = "+919834567892";
        when(jedis.del(phoneNo)).thenReturn(1L);

        // Call the method under test
        redisSetService.remove(phoneNo);

        // Verify that jedis.del() is called with the correct parameters
        verify(jedis, times(1)).del(phoneNo);
    }

    @Test
    public void testGetAllMembers() {
        // Mock the behavior of jedis.keys()
        Set<String> expectedResult = new HashSet<>();
        expectedResult.add("+919834567892");
        expectedResult.add("+917987654321");
        when(jedis.keys("+91*")).thenReturn(expectedResult);

        // Call the method under test
        Set<String> actualResult = redisSetService.findAll();

        // Verify that jedis.exists() is called with the correct parameter
        verify(jedis, times(1)).keys("+91*");

        // Assert the result
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsMember_present() {
        // Mock the behavior of jedis.sismember()
        String setName = "blacklistNumbers";
        String phoneNo = "+919834567892";
        when(jedis.exists(phoneNo)).thenReturn(true);

        // Call the method under test
        boolean result = redisSetService.isExists(phoneNo);

        // Verify that jedis.sismember() is called with the correct parameters
        verify(jedis, times(1)).exists(phoneNo);

        // Assert the result
        assertTrue(result);
    }

    @Test
    public void testIsMember_absent() {
        // Mock the behavior of jedis.exists()
        String setName = "blacklistNumbers";
        String phoneNo = "+919834567892";
        when(jedis.exists(phoneNo)).thenReturn(false);

        // Call the method under test
        boolean result = redisSetService.isExists(phoneNo);

        // Verify that jedis.exists() is called with the correct parameters
        verify(jedis, times(1)).exists(phoneNo);

        // Assert the result
        assertFalse(result);
    }
}
