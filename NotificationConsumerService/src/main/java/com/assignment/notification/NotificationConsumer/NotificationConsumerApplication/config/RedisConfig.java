package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Data
@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}") String host;
    @Value("${spring.data.redis.port}") int port;

    @Bean
    public Jedis jedis() {
        return new Jedis(host, port);
    }
}
