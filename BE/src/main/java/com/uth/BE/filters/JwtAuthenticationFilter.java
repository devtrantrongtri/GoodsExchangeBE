package com.uth.BE.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uth.BE.Service.JwtService;
import com.uth.BE.Service.MyUserDetailService;
import com.uth.BE.dto.res.GlobalRes;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// would be executed one per req
@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final MyUserDetailService myUserDetailService;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtService jwtService, MyUserDetailService myUserDetailService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.myUserDetailService = myUserDetailService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // move to another filers.
        }
        String jwt = authHeader.substring(7);
        String username;
        try {
            username = jwtService.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = myUserDetailService.loadUserByUsername(username); // lấy các thông tin từ database và gán vào userDetail form.

                if (userDetails != null && jwtService.isTokenValid(jwt)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, userDetails.getPassword(), userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // gán IP và URL từ yêu cầu hiện tại cho authentication
                    SecurityContextHolder.getContext().setAuthentication(authentication); // contextualize authentication for sýtem.
                }
            }
        }catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            GlobalRes<String> errorResponse = new GlobalRes<>(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
            }
        filterChain.doFilter(request, response);
    }
}
