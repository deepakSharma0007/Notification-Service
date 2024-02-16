package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.BlacklistNumber;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.repository.BlacklistNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlacklistNumberService{
    BlacklistNumberRepository blacklistNumberRepository;

    @Autowired
    public BlacklistNumberService(BlacklistNumberRepository blacklistNumberRepository)
    {
        this.blacklistNumberRepository = blacklistNumberRepository;
    }

    public List<BlacklistNumber> getAllNumbers()
    {
        return blacklistNumberRepository.findAll();
    }

    public void addBlacklistedNumber(BlacklistNumber blacklistNumber)
    {
        blacklistNumberRepository.save(blacklistNumber);
    }

    public Optional<BlacklistNumber> findByNumber(String phoneNo)
    {
        return blacklistNumberRepository.findByPhoneNumber(phoneNo);
    }

    public void deleteBlacklistedNumber(BlacklistNumber blacklistNumber)
    {
        blacklistNumberRepository.delete(blacklistNumber);
    }
}