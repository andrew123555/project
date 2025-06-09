package com.example.demo.websocket;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.example.demo.model.entity.Player;
import com.example.demo.model.entity.PlayerStats;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.PlayerStatsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;


import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component

public class GameWebSocketHandler extends TextWebSocketHandler {
	@Autowired
	private PlayerStatsRepository playerStatsRepository;

	@Autowired
	private PlayerRepository playerRepository;



    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, PlayerStats> userData = new ConcurrentHashMap<>();
    private static final Set<Session> sessions = ConcurrentHashMap.newKeySet();
    private static int targetNumber;
    private int guessCount;  // 🔹 記錄玩家猜測次數
    private long startTime;  // 🔹 記錄遊戲開始時間
    

    public GameWebSocketHandler() { // 🔹 無參數建構函式
        this.playerRepository = null; // 🔹 避免 Spring Boot 初始化時發生錯誤
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("✅ 玩家連線成功: " + session.getId());
    }

    

   

   

    @OnMessage
    public void onMessage(Session session, String message) {
        JsonObject jsonMessage = JsonParser.parseString(message).getAsJsonObject();
        String action = jsonMessage.get("action").getAsString();
        String username = jsonMessage.has("username") ? jsonMessage.get("username").getAsString() : "未知玩家";

        if (action.equals("guess") && userData.containsKey(username)) {
            PlayerStats stats = userData.get(username);
            stats.guessCount++;

            int guessedNumber = jsonMessage.get("number").getAsInt();
            if (guessedNumber == stats.targetNumber) {
                long totalTime = System.currentTimeMillis() - stats.startTime;
                double accuracy = (1.0 / stats.guessCount) * 100;

                // ✅ 存入 MySQL
                PlayerStats playerStats = new PlayerStats();
                playerStats.setUsername(username);
                playerStats.setGuessCount(stats.guessCount);
                playerStats.setTotalTime((int) (totalTime / 1000));
                playerStats.setAccuracy(accuracy);
                playerStatsRepository.save(playerStats);

                session.getAsyncRemote().sendText("🎉 恭喜，" + username + "！猜中數字：" + stats.targetNumber +
                    "\n📊 猜測次數: " + stats.guessCount + 
                    "\n🎯 正確率: " + String.format("%.2f", accuracy) + "%" +
                    "\n🕒 總時間: " + (totalTime / 1000) + " 秒");
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("⚠️ 玩家斷開: " + session.getId());
    }

    

   
}
