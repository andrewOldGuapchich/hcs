package com.andrew.authenticationserver.entity.dtos;

import lombok.Data;

@Data
public class PasswordDto {
    private String password;
    private String confirmPassword;
}
