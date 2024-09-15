package com.uth.BE.Config;

import com.uth.BE.filters.CustomHandshakeHandler;
import com.uth.BE.filters.JwtHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    public WebSocketConfig(JwtHandshakeInterceptor jwtHandshakeInterceptor) {
        this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        System.out.println("registerStompEndpoints");

        registry.addEndpoint("/ws")
                .addInterceptors(jwtHandshakeInterceptor)
                .setHandshakeHandler(new CustomHandshakeHandler())
                .setAllowedOriginPatterns("http://localhost:5173")
                .withSockJS()
                .setWebSocketEnabled(true);

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        System.out.println("configureMessageBroker");
        registry.enableSimpleBroker("/topic", "/queue"); // for public or private
        registry.setApplicationDestinationPrefixes("/app"); // for system controller
        registry.setUserDestinationPrefix("/user"); // for user
        

    }
}
