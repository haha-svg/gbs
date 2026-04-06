package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.VerificationCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/code")
@RequiredArgsConstructor
public class VerificationCodeController {
    
    private final VerificationCodeService verificationCodeService;
    
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> sendCode(@Valid @RequestBody SendCodeRequest request) {
        String code = verificationCodeService.generateCode(request.getTarget(), request.getType());
        return ResponseEntity.ok(ApiResponse.success("验证码发送成功", "验证码已发送（模拟）: " + code));
    }
}
