package com.uth.BE.Config;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent){
        // TODO -- to be implemented later
    }


}
