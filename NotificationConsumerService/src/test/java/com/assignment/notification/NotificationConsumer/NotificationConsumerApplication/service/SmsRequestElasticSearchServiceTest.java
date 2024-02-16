package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.SmsRequestElasticSearch;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.repository.SmsRequestElasticsearchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SmsRequestElasticSearchServiceTest {
    @Mock
    SmsRequestElasticsearchRepository smsRequestElasticsearchRepository;
    @InjectMocks
    SmsRequestElasticSearchService smsRequestElasticSearchService;

    @Test
    public void testSave() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
        Date createdAt = dateFormat.parse("11:02:2024 10:25:00");
        Date updatedAt = dateFormat.parse("11:02:2024 10:25:10");
        SmsRequestElasticSearch smsRequestElasticSearch = new SmsRequestElasticSearch(1, "abc", "+918003371989", "Test Message", "success", 0, null, createdAt, updatedAt);

        when(smsRequestElasticsearchRepository.save(smsRequestElasticSearch)).thenReturn(smsRequestElasticSearch);

        smsRequestElasticSearchService.save(smsRequestElasticSearch);

        verify(smsRequestElasticsearchRepository, times(1)).save(smsRequestElasticSearch);
    }

    @Test
    public void testFindByPhoneNumberAndTimeRange() {
        // Mock data
        String phoneNumber = "123456789";
        LocalDateTime startTime = LocalDateTime.of(2024, 2, 10, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 2, 11, 0, 0, 10);
        Pageable pageable = Pageable.unpaged();
        Page<SmsRequestElasticSearch> expectedResult = new PageImpl<>(Collections.emptyList());

        // Mock behavior of smsRequestElasticsearchRepository
        when(smsRequestElasticsearchRepository.findByPhoneNumberAndCreatedAtBetween(
                phoneNumber, Timestamp.valueOf(startTime), Timestamp.valueOf(endTime), pageable))
                .thenReturn(expectedResult);

        // Call the method under test
        Page<SmsRequestElasticSearch> actualResult = smsRequestElasticSearchService.findByPhoneNumberAndTimeRange(
                phoneNumber, startTime, endTime, pageable);

        // Verify that smsRequestElasticsearchRepository.findByPhoneNumberAndCreatedAtBetween() is called
        verify(smsRequestElasticsearchRepository, times(1))
                .findByPhoneNumberAndCreatedAtBetween(phoneNumber, Timestamp.valueOf(startTime),
                        Timestamp.valueOf(endTime), pageable);

        // Verify that the result matches the expected result
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFindByTextContaining()
    {
        // Mock data
        String searchText = "Test search text";
        Pageable pageable = Pageable.unpaged();
        Page<SmsRequestElasticSearch> expectedResult = new PageImpl<>(Collections.emptyList());

        // Mock behavior of smsRequestElasticsearchRepository
        when(smsRequestElasticsearchRepository.findByMessageContaining(searchText, pageable))
                .thenReturn(expectedResult);

        // Call the method under test
        Page<SmsRequestElasticSearch> actualResult = smsRequestElasticSearchService.findByTextContaining(searchText, pageable);

        // Verify that smsRequestElasticsearchRepository.findByMessageContaining() is called
        verify(smsRequestElasticsearchRepository, times(1))
                .findByMessageContaining(searchText, pageable);

        // Verify that the result matches the expected result
        assertEquals(expectedResult, actualResult);
    }
}
