package com.example.demo.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "player_stats")
public class PlayerStats {
    public static final int targetNumber = 0;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private int guessCount;
    private int totalTime;
    private double accuracy;
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getter & Setter
}
