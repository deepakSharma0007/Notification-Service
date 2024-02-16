package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Set;

@Service
public class RedisSetService {
    Jedis jedis;

    @Autowired
    public RedisSetService(Jedis jedis){
        this.jedis = jedis;
    }
    public void add(String phoneNo)
    {
        phoneNo = "BN::" + phoneNo;
        jedis.set(phoneNo, "1");
    }

    public void remove(String phoneNo)
    {
        phoneNo = "BN::" + phoneNo;
        jedis.del(phoneNo);
    }
    public Set<String> findAll()
    {
        return jedis.keys("BN::*");
    }

    public boolean isExists(String phoneNo)
    {
        phoneNo = "BN::" + phoneNo;
        return jedis.exists(phoneNo);
    }

    public void closeConnection()
    {
        jedis.close();
    }
}
