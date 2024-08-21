package com.uth.BE.Controller;

import com.uth.BE.Service.MyUserDetailService;
import com.uth.BE.dto.req.LoginReq;
import com.uth.BE.dto.res.GlobalRes;
import com.uth.BE.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;
    @Autowired
    MyUserDetailService myUserDetailService;

    @PostMapping("/authenticate")
    public GlobalRes<String> authenticateAndGetToken(@RequestBody LoginReq req) {
        try {
            // authenticate user with username and password, and generate new token for this session.
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
            if (authentication.isAuthenticated()) {
                String jwt = jwtService.generateToken(myUserDetailService.loadUserByUsername(req.getUsername()));
                return new GlobalRes<>(HttpStatus.OK, "Authentication successful", jwt);
            } else {
                return new GlobalRes<>(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }

        }catch (Exception e) {
            // Xử lý lỗi và trả về phản hồi lỗi
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Authentication failed: " + e.getMessage());
        }
    }
}
