package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.PasswordUtil;
import com.example.demo.util.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 模拟验证码存储
    private String verifyCode;

    public String generateVerifyCode() {
        verifyCode = VerifyCodeUtil.generateCode(6);
        // 实际项目中应该发送验证码到手机或邮箱
        System.out.println("验证码：" + verifyCode);
        return verifyCode;
    }

    public boolean verifyCode(String code) {
        return verifyCode != null && verifyCode.equals(code);
    }

    public User register(String phone, String email, String password, String nickname) {
        // 检查手机号是否已存在
        if (userRepository.findByPhone(phone).isPresent()) {
            throw new RuntimeException("手机号已注册");
        }

        // 检查邮箱是否已存在
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("邮箱已注册");
        }

        User user = new User();
        user.setPhone(phone);
        user.setEmail(email);
        user.setPassword(PasswordUtil.encrypt(password));
        user.setNickname(nickname);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        return userRepository.save(user);
    }

    public String login(String phone, String password) {
        Optional<User> userOptional = userRepository.findByPhone(phone);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("用户不存在");
        }

        User user = userOptional.get();
        if (!PasswordUtil.verify(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return jwtUtil.generateToken(user.getId());
    }

    public String loginByEmail(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("用户不存在");
        }

        User user = userOptional.get();
        if (!PasswordUtil.verify(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return jwtUtil.generateToken(user.getId());
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}