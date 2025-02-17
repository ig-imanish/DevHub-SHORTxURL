package com.shorter.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shorter.DTO.ShortenUrlRequest;
import com.shorter.DTO.ShortenUrlResponse;
import com.shorter.service.AuthService;
import com.shorter.service.ShortUrlService;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ShortUrlController {
    private final ShortUrlService shortUrlService;
    private final AuthService authService;

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody ShortenUrlRequest request, @RequestHeader("username") String username) {

        System.out.println(request);
        if (!authService.isUserAuthenticated(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(username + " is Unauthorized");
        }
        String originalUrl = request.getOriginalUrl().trim();
        if (originalUrl.isEmpty()) {
            return ResponseEntity.badRequest().body("Original URL cannot be empty");
        }
        
        if (request.getCustomName() != null && !request.getCustomName().trim().isEmpty()) {
            String customName = request.getCustomName().trim();
            if (customName.length() > 10) {
                return ResponseEntity.badRequest().body("Custom name must be less than 10 characters");
            }
        }
        
        String shortUrl = shortUrlService.createShortUrl(originalUrl, request.getCustomName(), username);
        if (shortUrl == null) {
            return ResponseEntity.badRequest().body("Custom name already taken");
        }
        return ResponseEntity.ok(new ShortenUrlResponse(shortUrl));
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> redirectToOriginal(@PathVariable String shortUrl ) {
        Optional<String> originalUrl = shortUrlService.getOriginalUrl(shortUrl);
        return originalUrl.map(url -> ResponseEntity.status(302).location(URI.create(url)).build())
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{shortUrl}")
    public ResponseEntity<?> deleteShortUrl(@PathVariable String shortUrl, @RequestHeader("username") String username) {
        if (!authService.isUserAuthenticated(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(username + "is Unauthorized");
        }
        if (shortUrl.isEmpty()) {
            return ResponseEntity.badRequest().body("Short URL cannot be empty");
        }
        shortUrl = shortUrl.trim();
        boolean deleted = shortUrlService.deleteShortUrl(shortUrl);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL not found");
    }
}
