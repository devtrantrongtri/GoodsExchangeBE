package com.uth.BE.Entity.model;

import com.uth.BE.Entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert the roles to a collection of GrantedAuthority
        return getRoles(user).stream()
                .map(role -> "ROLE_" + role)  // Add ROLE_ prefix
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private List<String> getRoles(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return List.of("CLIENT"); // Default role
        }
        return List.of(user.getRoles().split(","));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getUserId() {
        return user.getUserId();
    }
}
