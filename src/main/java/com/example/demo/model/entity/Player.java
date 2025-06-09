package com.example.demo.model.entity;



import jakarta.persistence.*;
import java.util.Random;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private int targetNumber = new Random().nextInt(100) + 1; // 🔢 1-100 隨機數
    private int attempts = 0;
    private boolean gameOver = false;

    public void checkGuess(int guess) {
        attempts++;
        if (guess == targetNumber) {
            gameOver = true;
        }
    }

	public boolean isGameOver() {
		  return gameOver;

	}
}