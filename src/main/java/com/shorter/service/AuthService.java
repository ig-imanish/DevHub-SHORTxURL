package com.shorter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${auth.api.url}")
    private String AUTH_API_URL;

    public boolean isUserAuthenticated(String username) {
        // try {
        //     Boolean response = restTemplate.getForObject(AUTH_API_URL + username, Boolean.class);
        //     return Boolean.TRUE.equals(response);
        // } catch (Exception e) {
        //     return false;
        // }
        return true;
    }
}

