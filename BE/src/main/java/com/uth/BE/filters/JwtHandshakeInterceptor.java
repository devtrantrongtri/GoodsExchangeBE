package com.uth.BE.filters;
import com.uth.BE.Entity.model.CustomUserDetails;
import com.uth.BE.Service.JwtService;
import com.uth.BE.Service.MyUserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtService jwtService;
    private final MyUserDetailService myUserDetailService;

    public JwtHandshakeInterceptor(JwtService jwtService, MyUserDetailService myUserDetailService) {
        this.jwtService = jwtService;
        this.myUserDetailService = myUserDetailService;
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String token = null;

            // Try to get token from the "Authorization" header
            String authHeader = servletRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            // If not found, try to get it from the query parameters
            if (token == null) {
                token = servletRequest.getParameter("token");
            }

            if (token != null) {
                try {
                    String username = jwtService.extractUsername(token);
                    if (username != null && jwtService.isTokenValid(token)) {
                        CustomUserDetails userDetails = myUserDetailService.loadUserByUsername(username);
                        if (userDetails != null) {
                            // Store the user details in attributes
                            attributes.put("user", userDetails);
                        }
                    } else {
                        return false; // Invalid token
                    }
                } catch (Exception e) {
                    return false; // Token parsing error
                }
            } else {
                return false; // No token provided
            }
        }
        return true; // Proceed with the handshake
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {
        // No action needed after handshake
    }
}

