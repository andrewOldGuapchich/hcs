package com.andrew.authserver.entity.dtos;

import lombok.Data;

@Data
public class RegistrationUserDto {
    public String email;
    public String password;
    public String confirmPassword;
    public int code;
}
