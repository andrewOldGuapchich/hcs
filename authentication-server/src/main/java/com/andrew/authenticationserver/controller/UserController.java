package com.andrew.authenticationserver.controller;

import com.andrew.authenticationserver.service.logic.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/close-user/{email}")
    public ResponseEntity<?> closeUser(@PathVariable("email") String email){
        return userService.closeUserCredential(email);
    }
}
