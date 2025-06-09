package com.example.demo.websocket;

import com.example.demo.model.entity.PlayerStats;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.PlayerStatsRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

	public GameWebSocketHandler(PlayerRepository playerRepository) {
	    this.playerRepository = playerRepository;
	}
	
    @Autowired
    private PlayerStatsRepository playerStatsRepository;

    @Autowired
    private PlayerRepository playerRepository;

    private final Map<String, PlayerStats> userData = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("✅ 玩家連線成功: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("⚠️ 玩家斷開: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonObject jsonMessage = JsonParser.parseString(message.getPayload()).getAsJsonObject();
        String action = jsonMessage.get("action").getAsString();
        String username = jsonMessage.has("username") ? jsonMessage.get("username").getAsString() : "未知玩家";

        userData.putIfAbsent(username, new PlayerStats());  // 初始化玩家資料
        PlayerStats stats = userData.get(username);

        if (action.equals("guess")) {
            stats.incrementGuessCount();
            int guessedNumber = jsonMessage.get("number").getAsInt();

            if (guessedNumber == stats.getTargetNumber()) {
                long totalTime = Duration.between(stats.getStartTime(), LocalDateTime.now()).toSeconds();
                double accuracy = (1.0 / stats.getGuessCount()) * 100;

                // 儲存到資料庫
                PlayerStats record = new PlayerStats();
                record.setUsername(username);
                record.setGuessCount(stats.getGuessCount());
                record.setTotalTime((int) totalTime);
                record.setAccuracy(accuracy);
                playerStatsRepository.save(record);

                session.sendMessage(new TextMessage("🎉 恭喜，" + username + "！猜中數字：" + stats.getTargetNumber() +
                        "\n📊 猜測次數: " + stats.getGuessCount() +
                        "\n🎯 正確率: " + String.format("%.2f", accuracy) + "%" +
                        "\n🕒 總時間: " + totalTime + " 秒"));
            } else {
                session.sendMessage(new TextMessage(guessedNumber > stats.getTargetNumber() ? "📉 太高了！" : "📈 太低了！"));
            }
        }
    }
}