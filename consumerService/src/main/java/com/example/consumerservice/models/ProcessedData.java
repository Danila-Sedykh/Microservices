package com.example.consumerservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "processed_data")
public class ProcessedData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private long openTime;

    @Column(nullable = false)
    private String openPrice;

    @Column(nullable = false)
    private String highPrice;

    @Column(nullable = false)
    private String lowPrice;

    @Column(nullable = false)
    private String closePrice;

    @Column(nullable = false)
    private String volume;

    @Column(nullable = false)
    private long closeTime;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;*/
}