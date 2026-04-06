package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SendCodeRequest {
    @Pattern(regexp = "^[1][3-9][0-9]{9}$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", 
             message = "请输入正确的手机号或邮箱")
    @NotBlank(message = "手机号或邮箱不能为空")
    private String target;
    
    @Pattern(regexp = "^(register|login|reset)$", message = "验证码类型不正确")
    @NotBlank(message = "验证码类型不能为空")
    private String type;
}
