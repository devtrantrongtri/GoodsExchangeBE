package com.uth.BE.filters;

import com.uth.BE.Entity.model.CustomUserDetails;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(
            ServerHttpRequest request, WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        // Retrieve the user details from the attributes
        CustomUserDetails userDetails = (CustomUserDetails) attributes.get("user");
        if (userDetails != null) {
            // Return a Principal with the user's username
            return new Principal() {
                @Override
                public String getName() {
                    return String.valueOf(userDetails.getUserId());
                }
            };
        }
        return null; // No authenticated user
    }
}
