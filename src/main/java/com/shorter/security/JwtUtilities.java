package com.shorter.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtilities {

    @Value("${auth.api.url}")
    private String API_BASE_URL;

    private RestTemplate restTemplate;

    public JwtUtilities(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getToken(HttpServletRequest httpServletRequest) {
        final String bearerToken = httpServletRequest.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        } // The part after "Bearer "
        return null;
    }

    public String validateTokenWithApi(String token) {
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

            org.springframework.http.ResponseEntity<Map> response = restTemplate.exchange(
                    API_BASE_URL + "/auth/validateToken",
                    org.springframework.http.HttpMethod.POST,
                    entity,
                    Map.class);

            if (response.getStatusCode() == org.springframework.http.HttpStatus.OK) {
                if (response.getBody() != null && response.getBody().containsKey("email")) {
                    return (String) response.getBody().get("email");
                }
                return null;
            }
            return null;
        } catch (Exception e) {
            log.error("Error validating token with API: {}", e.getMessage());
            return null;
        }
    }

}
