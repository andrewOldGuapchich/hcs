package com.andrew.authenticationserver.service.registration;

import com.andrew.authenticationserver.entity.dtos.RegistrationUserDto;
import com.andrew.authenticationserver.entity.dtos.jwt.JwtRequest;
import com.andrew.authenticationserver.service.additional.EmailService;
import com.andrew.authenticationserver.service.authentication.AuthenticationService;
import com.andrew.authenticationserver.service.logic.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final EmailService emailService;
    private final UserService userService;
    private final AuthenticationService authService;

    public ResponseEntity<?> registrationUser(RegistrationUserDto registrationUserDto) {
        if (userService.findByEmail(registrationUserDto.getEmail()).get().getCode() != registrationUserDto.getCode()) {
            return ResponseEntity.badRequest().body("Код не верный!");
        } else {
            userService.updateUserStatus(registrationUserDto.getEmail());
            JwtRequest jwtRequest = new JwtRequest();
            jwtRequest.setEmail(registrationUserDto.getEmail());
            jwtRequest.setPassword(registrationUserDto.getPassword());
            return authService.createAuthToken(jwtRequest);
        }
    }

    public ResponseEntity<?> sendEmailCode (RegistrationUserDto registrationUserDto){
        if(!registrationUserDto.getPassword()
                .equals(registrationUserDto.getConfirmPassword())){
            return ResponseEntity.badRequest().body("Пароли не совпадают!");
        }
        if(userService.findByEmail(registrationUserDto.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body("Такой пользователь уже зарегистрирован!");
        }
        int code = generateCode();
        userService.createUserWaitingStatus(registrationUserDto, code);
        sendPassword(registrationUserDto.getEmail(), code);
        return ResponseEntity.ok("Send message");
    }


    private void sendPassword(String email, int code){
        String subject = "ЖКХ код";
        String text = "Code - " + code;
        emailService.sendEmail(email, subject, text);
    }

    private int generateCode(){
        return new Random().nextInt(1000, 10000);
    }
}

