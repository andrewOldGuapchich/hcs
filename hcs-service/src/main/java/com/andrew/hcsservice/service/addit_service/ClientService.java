package com.andrew.hcsservice.service.addit_service;

import com.andrew.hcsservice.model.dto.auth.AuthDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final RestTemplate restTemplate;
    @Value("${auth.config.url}")
    private String authUrl;
    @Value("${auth.config.port}")
    private String authPort;

    public AuthDTO authorizeUser(String jwtToken){
        String url = UriComponentsBuilder.fromHttpUrl(authUrl + authPort + "/authorize/byToken")
                .encode()
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<AuthDTO> response = restTemplate
                .exchange(url, HttpMethod.GET, entity, AuthDTO.class);

        return response.getBody();
    }
}
