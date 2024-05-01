package com.andrew.hcsservice.service.auth_service;

import com.andrew.hcsservice.model.dto.auth.AuthDTO;
import com.andrew.hcsservice.model.dto.auth.RegUser;
import com.andrew.hcsservice.service.addit_service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService implements UserDetailsService {
    private final ClientService clientService;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        AuthDTO authDTO = clientService.authorizeUser(token);
        return new RegUser(authDTO.getEmail(), authDTO.getRoles());
    }
}
