package com.example.demo.security;

import com.example.demo.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtUtil.generateToken(principal.getId(), principal.getUsername());
        
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                new java.util.HashMap<String, Object>() {{
                    put("code", 200);
                    put("message", "success");
                    put("data", new java.util.HashMap<String, Object>() {{
                        put("token", token);
                        put("tokenType", "Bearer");
                    }});
                }}
        ));
    }
}
