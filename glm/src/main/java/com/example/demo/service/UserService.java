package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final VerificationCodeService verificationCodeService;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (request.getPhone() == null || request.getPhone().isBlank()) {
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                throw new RuntimeException("手机号或邮箱必须填写一个");
            }
        }
        
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            if (userRepository.existsByPhone(request.getPhone())) {
                throw new RuntimeException("该手机号已被注册");
            }
            if (!verificationCodeService.verifyCode(request.getPhone(), request.getCode(), "register")) {
                throw new RuntimeException("验证码错误或已过期");
            }
        }
        
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("该邮箱已被注册");
            }
            if (!verificationCodeService.verifyCode(request.getEmail(), request.getCode(), "register")) {
                throw new RuntimeException("验证码错误或已过期");
            }
        }
        
        User user = new User();
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        
        userRepository.save(user);
        
        String token = jwtUtil.generateToken(user.getId(), user.getNickname());
        return new AuthResponse(token, user.getId(), user.getNickname());
    }
    
    public AuthResponse login(LoginRequest request) {
        User user = null;
        
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            user = userRepository.findByPhone(request.getPhone())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
        } else if (request.getEmail() != null && !request.getEmail().isBlank()) {
            user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
        } else {
            throw new RuntimeException("请输入手机号或邮箱");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        String token = jwtUtil.generateToken(user.getId(), user.getNickname());
        return new AuthResponse(token, user.getId(), user.getNickname());
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }
}
