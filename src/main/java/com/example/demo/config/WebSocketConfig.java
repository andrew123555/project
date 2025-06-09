package com.example.demo.config;




import com.example.demo.websocket.GameWebSocketHandler;
import com.example.demo.repository.PlayerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public GameWebSocketHandler gameWebSocketHandler(PlayerRepository playerRepository) {
        return new GameWebSocketHandler(playerRepository);
    }
}