package com.example.demo.util;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordUtil {
    public static String encrypt(String password) {
        return DigestUtils.md5Hex(password);
    }

    public static boolean verify(String password, String encryptedPassword) {
        return DigestUtils.md5Hex(password).equals(encryptedPassword);
    }
}