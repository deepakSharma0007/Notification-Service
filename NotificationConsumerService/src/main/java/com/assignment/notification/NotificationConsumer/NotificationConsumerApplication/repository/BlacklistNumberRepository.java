package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.repository;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.BlacklistNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlacklistNumberRepository extends JpaRepository<BlacklistNumber, String> {
    public Optional<BlacklistNumber> findByPhoneNumber(String phoneNo);
}
