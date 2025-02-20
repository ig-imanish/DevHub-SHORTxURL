package com.shorter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${auth.api.url}")
    private String AUTH_API_URL;

    @Value("${auth.api.url.extractUsername}")
    private String AUTH_API_URL_EXTRACT_USERNAME;

    public boolean isUserAuthenticated(String token) {
        try {
            String username = extractUsername(token);
            if (username == null) {
                return false;
            }

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setBearerAuth(token);
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

            org.springframework.http.ResponseEntity<Boolean> response = restTemplate.exchange(
                    AUTH_API_URL,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    Boolean.class);
            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setBearerAuth(token);
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

            org.springframework.http.ResponseEntity<String> response = restTemplate.exchange(
                    AUTH_API_URL_EXTRACT_USERNAME,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    String.class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
