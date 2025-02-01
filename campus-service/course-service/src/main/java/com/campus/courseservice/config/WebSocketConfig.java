package com.campus.courseservice.config;

import com.campus.courseservice.inteceptor.CustomWebsocketInterceptor;
import com.campus.courseservice.websocket.WebSocketServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private CustomWebsocketInterceptor customWebsocketInterceptor;

    @Resource
    private WebSocketServer customWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(customWebSocketHandler, "/ws")
                .setAllowedOrigins("*")
                .addInterceptors(customWebsocketInterceptor);
    }
}