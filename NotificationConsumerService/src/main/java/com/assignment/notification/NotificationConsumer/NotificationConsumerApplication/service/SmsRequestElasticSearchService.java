package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.SmsRequestElasticSearch;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.repository.SmsRequestElasticsearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SmsRequestElasticSearchService {
    private final SmsRequestElasticsearchRepository smsRequestElasticsearchRepository;

    @Autowired
    SmsRequestElasticSearchService(SmsRequestElasticsearchRepository smsRequestElasticsearchRepository)
    {
        this.smsRequestElasticsearchRepository = smsRequestElasticsearchRepository;
    }

    public void save(SmsRequestElasticSearch smsRequestElasticSearch)
    {
        smsRequestElasticsearchRepository.save(smsRequestElasticSearch);
    }


    public Page<SmsRequestElasticSearch> findByPhoneNumberAndTimeRange(
            String phoneNumber, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        return smsRequestElasticsearchRepository.findByPhoneNumberAndCreatedAtBetween(
                phoneNumber, Timestamp.valueOf(startTime), Timestamp.valueOf(endTime), pageable);
    }

    public Page<SmsRequestElasticSearch> findByTextContaining(String searchText, Pageable pageable) {
        return smsRequestElasticsearchRepository.findByMessageContaining(searchText, pageable);
    }
}
