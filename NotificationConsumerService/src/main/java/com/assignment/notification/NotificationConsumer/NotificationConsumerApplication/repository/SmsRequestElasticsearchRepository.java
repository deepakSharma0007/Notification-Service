package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.repository;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.SmsRequestElasticSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface SmsRequestElasticsearchRepository extends ElasticsearchRepository<SmsRequestElasticSearch, String> {
//    this return all the sms sent by given phone number and in between given time interval
    Page<SmsRequestElasticSearch> findByPhoneNumberAndCreatedAtBetween(
            String phoneNumber, Timestamp startTime, Timestamp endTime, Pageable pageable);

//    this return all sms containing given text
    @Query("{\"match_phrase\":{\"message\":{\"query\":\"?0\"}}}")
    Page<SmsRequestElasticSearch> findByMessageContaining(String searchText, Pageable pageable);
}
