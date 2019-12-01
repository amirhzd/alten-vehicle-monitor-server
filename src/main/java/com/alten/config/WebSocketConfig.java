package com.alten.config;

import com.alten.service.dto.CustomerVehicleStatusFilter;
import com.alten.service.WebsocketSubscribersMap;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketHandler implements WebSocketMessageBrokerConfigurer {

    private final WebsocketSubscribersMap<CustomerVehicleStatusFilter> websocketSubscribersMap;

    public WebSocketConfig(WebsocketSubscribersMap<CustomerVehicleStatusFilter> websocketSubscribersMap) {
        this.websocketSubscribersMap = websocketSubscribersMap;
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
            .addEndpoint("/ws")
            .setAllowedOrigins("http://localhost:4200")
            .withSockJS();
    }


    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        websocketSubscribersMap.unsubscribe(webSocketSession.getId());
    }
}
