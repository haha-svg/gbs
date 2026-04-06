package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.UserService;
import com.example.demo.service.VerificationCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    private final VerificationCodeService verificationCodeService;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = userService.register(request);
        return ResponseEntity.ok(ApiResponse.success("注册成功", response));
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("登录成功", response));
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.getUserById(principal.getId());
        user.setPassword(null);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
}
