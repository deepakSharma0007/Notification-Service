package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "sms_requests")
@Data
public class SmsRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "request_id", unique = true)
    private String requestId;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String message;
    private String status;
    @Column(name = "failure_code")
    private int failureCode;
    @Column(name = "failure_comments")
    private String failureComments;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public SmsRequest() {
    }
}
