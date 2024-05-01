package com.andrew.authenticationserver.entity.dtos.jwt;

import lombok.Data;

@Data
public class JwtRequest {
    private String email;
    private String password;
}
