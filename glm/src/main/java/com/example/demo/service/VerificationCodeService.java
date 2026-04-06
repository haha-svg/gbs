package com.example.demo.service;

import com.example.demo.entity.VerificationCode;
import com.example.demo.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {
    
    private final VerificationCodeRepository verificationCodeRepository;
    private static final int CODE_LENGTH = 6;
    private static final int EXPIRE_MINUTES = 5;
    
    @Transactional
    public String generateCode(String target, String type) {
        String code = generateRandomCode();
        
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setTarget(target);
        verificationCode.setCode(code);
        verificationCode.setType(type);
        verificationCode.setExpireAt(LocalDateTime.now().plusMinutes(EXPIRE_MINUTES));
        
        verificationCodeRepository.save(verificationCode);
        return code;
    }
    
    public boolean verifyCode(String target, String code, String type) {
        return verificationCodeRepository
                .findByTargetAndCodeAndTypeAndUsedFalseAndExpireAtAfter(
                        target, code, type, LocalDateTime.now())
                .map(vc -> {
                    vc.setUsed(true);
                    verificationCodeRepository.save(vc);
                    return true;
                })
                .orElse(false);
    }
    
    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
