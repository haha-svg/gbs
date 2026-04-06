package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/verify-code")
    public Map<String, Object> generateVerifyCode() {
        String code = userService.generateVerifyCode();
        return Map.of("code", code, "message", "验证码已生成");
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String email = params.get("email");
        String password = params.get("password");
        String nickname = params.get("nickname");
        String verifyCode = params.get("verifyCode");

        if (!userService.verifyCode(verifyCode)) {
            return Map.of("code", 400, "message", "验证码错误");
        }

        try {
            userService.register(phone, email, password, nickname);
            return Map.of("code", 200, "message", "注册成功");
        } catch (Exception e) {
            return Map.of("code", 400, "message", e.getMessage());
        }
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String password = params.get("password");

        try {
            String token = userService.login(phone, password);
            return Map.of("code", 200, "message", "登录成功", "token", token);
        } catch (Exception e) {
            return Map.of("code", 400, "message", e.getMessage());
        }
    }

    @PostMapping("/login/email")
    public Map<String, Object> loginByEmail(@RequestBody Map<String, String> params) {
        String email = params.get("email");
        String password = params.get("password");

        try {
            String token = userService.loginByEmail(email, password);
            return Map.of("code", 200, "message", "登录成功", "token", token);
        } catch (Exception e) {
            return Map.of("code", 400, "message", e.getMessage());
        }
    }
}