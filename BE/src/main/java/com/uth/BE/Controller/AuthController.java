package com.uth.BE.Controller;

import com.uth.BE.Service.MyUserDetailService;
import com.uth.BE.dto.req.LoginReq;
import com.uth.BE.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipalNotFoundException;

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
    public String authenticateAndGetToken(@RequestBody LoginReq req){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(),req.getPassword()));
        if(authentication.isAuthenticated()){
            // NOTE : return a UserDetail that was config in MyUserDetailService (attribute of SpringBoot architecture )
            return jwtService.generateToken(myUserDetailService.loadUserByUsername(req.getUsername()));
        }else {
            throw new UsernameNotFoundException("Invalid credentials");
        }

    }
}
