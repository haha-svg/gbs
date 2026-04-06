package com.example.demo.util;

import java.util.Random;

public class VerifyCodeUtil {
    public static String generateCode(int length) {
        String chars = "0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }
}