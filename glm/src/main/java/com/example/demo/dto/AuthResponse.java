package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String nickname;
    
    public AuthResponse(String token, Long userId, String nickname) {
        this.token = token;
        this.userId = userId;
        this.nickname = nickname;
    }
}
