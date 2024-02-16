package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.repository;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.SmsRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmsRequestRepository extends JpaRepository<SmsRequest, String> {
    Optional<SmsRequest> findByRequestId(String requestId);
}
