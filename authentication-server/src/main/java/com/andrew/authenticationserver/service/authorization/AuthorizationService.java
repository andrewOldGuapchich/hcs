package com.andrew.authenticationserver.service.authorization;

import com.andrew.authenticationserver.entity.auth.Role;
import com.andrew.authenticationserver.entity.auth.User;
import com.andrew.authenticationserver.entity.dtos.auth.AuthDto;
import com.andrew.authenticationserver.exeption.AppError;
import com.andrew.authenticationserver.repository.UserRepository;
import com.andrew.authenticationserver.service.additional.JwtTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorizationService {
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;

    public ResponseEntity<?> tokenAuthorize(String token){
        System.out.println("token - " + token);
        //token = substringToken(token);
        if (token.isEmpty()){
            return ResponseEntity.ok().build();
        }
        else if(jwtTokenService.validateAccessToken(token)){
            String email = jwtTokenService.getUsername(token);
            Optional<User> user = userRepository.findByEmail(email);
            if(user.isEmpty())
                return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "User not found!"), HttpStatus.BAD_REQUEST);
            List<String> roles = mapRoleOnString(user.get().getRoles());

            return ResponseEntity.ok(new AuthDto(email, roles, true));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    private List<String> mapRoleOnString(List<Role> roles){
        return roles.stream()
                .map(Role::getRole)
                .collect(Collectors.toList());
    }

    private String substringToken(String token){
        token = token.substring(7);
        return token;
    }
}
