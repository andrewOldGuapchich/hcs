package com.andrew.authenticationserver.entity.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthDto {
    private String email;
    //public String hashPassword;
    private List<String> roles;
    private boolean isAuthorize;
}
