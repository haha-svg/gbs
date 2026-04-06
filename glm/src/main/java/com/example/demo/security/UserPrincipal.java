package com.example.demo.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.security.Principal;

@Data
@AllArgsConstructor
public class UserPrincipal implements Principal {
    private Long id;
    private String username;
    
    @Override
    public String getName() {
        return username;
    }
}
