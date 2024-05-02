package com.andrew.hcsservice.model.dto.auth;

import lombok.Data;

import java.util.List;

@Data
public class AuthDto {
    private String email;
    private String password;
    private List<String> roles;
}
