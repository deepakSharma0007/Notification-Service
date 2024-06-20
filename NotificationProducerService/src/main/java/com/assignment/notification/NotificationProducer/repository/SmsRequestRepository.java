package com.assignment.notification.NotificationProducer.repository;

import com.assignment.notification.NotificationProducer.entity.SmsRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SmsRequestRepository extends JpaRepository<SmsRequest, String> {
    public Optional<SmsRequest> findByRequestId(String requestId);
}
