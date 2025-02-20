package com.shorter.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shorter.DTO.ShortenUrlError;
import com.shorter.DTO.ShortenUrlRequest;
import com.shorter.DTO.ShortenUrlResponse;
import com.shorter.model.ShortUrl;
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
    public ResponseEntity<?> shortenUrl(@RequestBody ShortenUrlRequest request,
            @RequestHeader("username") String username) {

        System.out.println(request);
        if (!authService.isUserAuthenticated(username)) {
            // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(username + " is
            // Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ShortenUrlError(false, username + " is UNAUTHORIZED", new Date()));
        }
        String originalUrl = request.getOriginalUrl().trim();
        if (originalUrl.isEmpty()) {
            // return ResponseEntity.badRequest().body("Original URL cannot be empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ShortenUrlError(false, "Original URL cannot be empty", new Date()));
        }

        if (request.getCustomName() != null && !request.getCustomName().trim().isEmpty()) {
            String customName = request.getCustomName().trim();
            if (customName.length() > 10) {
                // return ResponseEntity.badRequest().body("Custom name must be less than 10
                // characters");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ShortenUrlError(false, "Custom name must be less than 10 characters", new Date()));
            }
        }
        String expirationTime = null;
        if (request.getExpirationTime() != null && !request.getExpirationTime().trim().isEmpty()) {
            expirationTime = request.getExpirationTime().trim();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                Date expirationDate = sdf.parse(expirationTime);
                Date currentDate = new Date();

                if (expirationDate.before(currentDate)) {
                    // return ResponseEntity.badRequest().body("Expiration time cannot be in the
                    // past");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ShortenUrlError(false, "Expiration time cannot be in the past", new Date()));
                }
            } catch (ParseException e) {
                // return ResponseEntity.badRequest()
                // .body("Invalid date format. Use format: 'EEE MMM dd HH:mm:ss zzz yyyy' (e.g.,
                // 'Wed Mar 20 15:30:00 EDT 2024')");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ShortenUrlError(false,
                        "Invalid date format. Use format: 'EEE MMM dd HH:mm:ss zzz yyyy' (e.g., 'Wed Mar 20 15:30:00 EDT 2024')",
                        new Date()));
            }
        }

        ShortUrl shortUrl = shortUrlService.createShortUrl(originalUrl, request.getCustomName(), username,
                expirationTime);
        if (shortUrl == null) {
            // return ResponseEntity.badRequest().body("Custom name already taken");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ShortenUrlError(false, "Custom name already taken", new Date()));
        }
        return ResponseEntity.ok(new ShortenUrlResponse(true, shortUrl.getId(), shortUrl.getOriginalUrl(),
                shortUrl.getShortUrl(), shortUrl.getUsername(), shortUrl.getCreatedAt(), shortUrl.getExpirationTime()));
    }

    @DeleteMapping("/delete/{shortUrl}")
    public ResponseEntity<?> deleteShortUrl(@PathVariable String shortUrl, @RequestHeader("username") String username) {
        if (!authService.isUserAuthenticated(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ShortenUrlError(false, username + " is UNAUTHORIZED", new Date()));
        }
        if (shortUrl.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ShortenUrlError(false, "Short URL cannot be empty", new Date()));
        }
        shortUrl = shortUrl.trim();
        boolean deleted = shortUrlService.deleteShortUrl(shortUrl);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ShortenUrlError(false, "URL not found", new Date()));
    }
}
