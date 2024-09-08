package com.uth.BE.dto.req;

import com.uth.BE.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Integer userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private String role;
    private LocalDateTime createdAt;

    public static  UserDTO convertToDTO(User user) {
        // Chuyển đổi User sang UserDTO
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .address(user.getAddress())
                .role(user.getRoles())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
