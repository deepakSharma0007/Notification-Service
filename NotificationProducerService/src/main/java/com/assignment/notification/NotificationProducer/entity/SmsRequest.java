package com.assignment.notification.NotificationProducer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("request_id")
    @Column(name = "request_id", unique = true)
    private String requestId;
    @JsonProperty("phone_number")
    @Column(name = "phone_number")
    private String phoneNumber;
    private String message;
    private String status;
    @JsonProperty("failure_code")
    @Column(name = "failure_code")
    private int failureCode;
    @JsonProperty("failure_comments")
    @Column(name = "failure_comments")
    private String failureComments;
    @JsonProperty("created_at")
    @Column(name = "created_at")
    private Timestamp createdAt;
    @JsonProperty("updated_at")
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public SmsRequest() {
    }

    public SmsRequest(int id, String requestId, String phoneNumber, String message, String status, int failureCode, String failureComments, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.requestId = requestId;
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.status = status;
        this.failureCode = failureCode;
        this.failureComments = failureComments;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Sms_requests{" +
                "request_id=" + requestId +
                ", phone_number='" + phoneNumber + '\'' +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", failure_code=" + failureCode +
                ", failure_comments='" + failureComments + '\'' +
                ", created_at=" + createdAt +
                ", updated_at=" + updatedAt +
                '}';
    }
}


