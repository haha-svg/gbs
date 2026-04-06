package com.example.demo.repository;

import com.example.demo.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByTargetAndCodeAndTypeAndUsedFalseAndExpireAtAfter(
        String target, String code, String type, LocalDateTime now);
}
