package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "blacklist_numbers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;
}
