package com.shorter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    // private final RestTemplate restTemplate = new RestTemplate();
    // private final JwtUtil jwtUtil;

    // // @Value("${auth.api.url}")
    // // private String AUTH_API_URL;

    // // @Value("${auth.api.url.extractUsername}")
    // // private String AUTH_API_URL_EXTRACT_USERNAME;

    // // ✅ Use @Autowired constructor for jwtUtil
    // public AuthService(JwtUtil jwtUtil) {
    // this.jwtUtil = jwtUtil;
    // }

    // public boolean isUserAuthenticated(String token) {
    // return jwtUtil.isTokenValid(token);
    // }

    // public String extractUsername(String token) {
    // return jwtUtil.extractUsername(token);
    // }
}
