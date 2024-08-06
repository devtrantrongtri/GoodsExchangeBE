package com.uth.BE.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class DataHandler extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(DataHandler.class);

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        log.info("Received message: {}", message.getPayload());
        try {
            session.sendMessage(message);  // Echo the message back
        } catch (Exception e) {
            log.error("Error while sending message back to client", e);
        }
    }
}
