package com.andrew.authenticationserver.service.registration;

import com.andrew.authenticationserver.entity.dtos.PasswordDto;
import com.andrew.authenticationserver.entity.dtos.RegistrationUserDto;
import com.andrew.authenticationserver.entity.dtos.jwt.JwtRequest;
import com.andrew.authenticationserver.service.additional.EmailService;
import com.andrew.authenticationserver.service.authentication.AuthenticationService;
import com.andrew.authenticationserver.service.logic.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Random;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final EmailService emailService;
    private final UserService userService;
    private final AuthenticationService authService;
    private final RestTemplate restTemplate;

    public ResponseEntity<?> registrationUser(RegistrationUserDto registrationUserDto) {
        if (userService.findWaitingByEmail(registrationUserDto.getEmail()).get().getCode() != registrationUserDto.getCode()) {
            return ResponseEntity.badRequest().body("Код не верный!");
        } else {
            userService.updateUserStatus(registrationUserDto.getEmail());
            JwtRequest jwtRequest = new JwtRequest();
            jwtRequest.setEmail(registrationUserDto.getEmail());
            jwtRequest.setPassword(registrationUserDto.getPassword());
            sendRequestLogicServer(registrationUserDto.getEmail());
            return authService.createAuthToken(jwtRequest);
        }
    }

    private void sendRequestLogicServer(String email){
        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:7171/owner/activate-user/" + email)
                .encode()
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        restTemplate
                .exchange(url, HttpMethod.POST, entity, Object.class);
    }


    public ResponseEntity<?> sendEmailCode (PasswordDto passwordDto, String email){
        if(!passwordDto.getPassword()
                .equals(passwordDto.getConfirmPassword())){
            return ResponseEntity.badRequest().body("Пароли не совпадают!");
        }
        else if(userService.findByEmail(email).isPresent()){
            return ResponseEntity.badRequest().body("Такой пользователь уже зарегистрирован!");
        }
        int code = generateCode();

        RegistrationUserDto registrationUserDto = new RegistrationUserDto();
        registrationUserDto.setPassword(passwordDto.getPassword());
        registrationUserDto.setConfirmPassword(passwordDto.getConfirmPassword());
        registrationUserDto.setEmail(email);
        registrationUserDto.setCode(code);

        userService.createUserWaitingStatus(registrationUserDto);
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

