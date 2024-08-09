package com.uth.BE.Config;

//import com.uth.BE.ws.DataHandler;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(dataHandler(), "/data").setAllowedOrigins("*");
//    }
//
//    @Bean
//    public WebSocketHandler dataHandler() {
//        return new DataHandler();
//    }
//}

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // for public or private
        registry.setApplicationDestinationPrefixes("/app"); // for system controller
//        registry.setUserDestinationPrefix("/user"); // for user

//        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);

    }
}
