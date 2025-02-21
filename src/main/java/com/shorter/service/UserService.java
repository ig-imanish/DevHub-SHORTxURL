package com.shorter.service;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shorter.models.Role;
import com.shorter.models.RoleName;
import com.shorter.models.User;

import org.springframework.web.client.RestClientException;

@Service
public class UserService {

    // private final RestTemplate restTemplate;
    // private final String API_BASE_URL = "http://localhost:8087/api/api/v1/users";

    // public UserService(RestTemplate restTemplate) {
    // this.restTemplate = restTemplate;
    // }

    // public User getUserDetails(String token) {
    // String url = API_BASE_URL + "/token";

    // // Add authorization header
    // HttpHeaders headers = new HttpHeaders();
    // headers.setBearerAuth(token);

    // HttpEntity<String> entity = new HttpEntity<>(headers);

    // try {
    // ResponseEntity<User> response = restTemplate.exchange(
    // url,
    // HttpMethod.GET,
    // entity,
    // User.class);
    // return response.getBody();
    // } catch (RestClientException e) {
    // throw new RuntimeException("Failed to fetch user details: " +
    // e.getMessage());
    // }
    // }

    public User findByEmail(String email) {
        User user = new User("admin@gmail.com", "admin", List.of(new Role(RoleName.ADMIN)));

        return user;
        // String url = API_BASE_URL + "/email/" + email;

        // try {
        // ResponseEntity<User> response = restTemplate.exchange(
        // url,
        // HttpMethod.GET,
        // null, // No headers needed if this is a public endpoint
        // User.class);
        // return response.getBody();
        // } catch (RestClientException e) {
        // throw new RuntimeException("Failed to fetch user by email: " +
        // e.getMessage());
        // }
    }

}
