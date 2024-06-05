package com.andrew.authenticationserver.controller;

import com.andrew.authenticationserver.service.authorization.AuthorizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/authorize")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @GetMapping("/byToken")
    public ResponseEntity<?> authorize(@RequestHeader("Authorization") String token){
        return authorizationService.tokenAuthorize(token);
    }
}
