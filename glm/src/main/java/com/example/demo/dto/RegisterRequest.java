package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @Pattern(regexp = "^$|^[1][3-9][0-9]{9}$", message = "手机号格式不正确")
    private String phone;

    @Pattern(regexp = "^$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String code;

    @NotBlank(message = "昵称不能为空")
    private String nickname;
}
