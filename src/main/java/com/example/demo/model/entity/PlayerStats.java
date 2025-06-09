package com.example.demo.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name = "player_stats")
public class PlayerStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private int guessCount;
    private int totalTime;
    private double accuracy;
    private int targetNumber = new Random().nextInt(100) + 1;  // ✅ 每位玩家隨機目標數字
    private LocalDateTime startTime = LocalDateTime.now();  // ✅ 新增 startTime 欄位
    private LocalDateTime createdAt = LocalDateTime.now();


    public int getTargetNumber() {
        return targetNumber;
    }

    public void setTargetNumber(int targetNumber) {
        this.targetNumber = targetNumber;
    }
    public int getGuessCount() {
        return guessCount;
    }

    public void setGuessCount(int guessCount) {
        this.guessCount = guessCount;
    }

    public void incrementGuessCount() {
        this.guessCount++;
    }

    public long getStartTimeMillis() {
        return startTime != null ? startTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() : 0;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}