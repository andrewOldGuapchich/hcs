package com.andrew.authenticationserver.service.authentication;

import com.andrew.authenticationserver.entity.dtos.jwt.JwtRequest;
import com.andrew.authenticationserver.entity.dtos.jwt.JwtResponse;
import com.andrew.authenticationserver.exeption.AppError;
import com.andrew.authenticationserver.service.additional.JwtTokenService;
import com.andrew.authenticationserver.service.logic.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    public ResponseEntity<?> createAuthToken(JwtRequest authRequest){
        try{
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    ));
        } catch (BadCredentialsException e){
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(), "Incorrect login or password!"), HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
        String jwtToken = jwtTokenService.generateAccessToken(userDetails);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", jwtToken);
        System.out.println("Authenticated");

        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }
}
