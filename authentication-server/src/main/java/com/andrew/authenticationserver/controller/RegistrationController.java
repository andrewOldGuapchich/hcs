package com.andrew.authenticationserver.controller;

import com.andrew.authenticationserver.entity.dtos.RegistrationUserDto;
import com.andrew.authenticationserver.service.registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final RegistrationService registrationService;


    @PostMapping("/send-ver-code/{email}")
    public ResponseEntity<?> send(@RequestBody RegistrationUserDto registrationUserDto, @PathVariable String email){
        return registrationService.sendEmailCode(registrationUserDto);
    }

    @PostMapping("/add-user")
    public ResponseEntity<?> info(@RequestBody RegistrationUserDto registrationUserDto){
        return registrationService.registrationUser(registrationUserDto);
    }
}
