package com.shorter.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shorter.DTO.ShortenUrlError;
import com.shorter.DTO.ShortenUrlRequest;
import com.shorter.DTO.ShortenUrlResponse;
import com.shorter.helper.Helper;
import com.shorter.models.ShortUrl;
import com.shorter.premium.PremiumRequired;
import com.shorter.repo.ShortUrlRepository;
import com.shorter.service.ShortUrlService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ShortUrlController {
    private final ShortUrlService shortUrlService;
    private final ShortUrlRepository shortUrlRepository;

    @GetMapping("/hello-world")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Hello World");
    }

    @PremiumRequired
    @GetMapping("/premium")
    public ResponseEntity<String> getPremiumData() {
        return ResponseEntity.ok("This is premium data");
    }

    @GetMapping("/shorten")
    public ResponseEntity<?> getUrl() {
        List<ShortUrl> shortUrls = shortUrlRepository.findAll();
        return ResponseEntity.ok(shortUrls);
    }

    @GetMapping("/shorten/user/{username}")
    public ResponseEntity<?> getUrlByUser(@PathVariable String username) {
        if (username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ShortenUrlError(false, "Username cannot be empty", new Date()));
        }
        List<ShortUrl> shortUrls = shortUrlRepository.findByUsername(username);
        return ResponseEntity.ok(shortUrls);
    }

    @GetMapping("/shorten/me")
    public ResponseEntity<?> getUrlByAuthUser(Principal principal) {
        String username = principal.getName();
        List<ShortUrl> shortUrls = shortUrlRepository.findByUsername(username);
        return ResponseEntity.ok(shortUrls);
    }

    @GetMapping("/shorten/{shortUrl}")
    public ResponseEntity<?> getUrlByCustomName(@PathVariable String shortUrl) {
        Optional<ShortUrl> shortUrlObj = shortUrlRepository.findByShortUrl(shortUrl);
        if (shortUrlObj.isPresent()) {
            return ResponseEntity.ok(shortUrlObj.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ShortenUrlError(false, "URL not found", new Date()));
    }

    @PremiumRequired
    @PostMapping("/shorten/custom")
    public ResponseEntity<?> shortenUrlCustom(@RequestBody ShortenUrlRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ShortenUrlError(false, "Unauthorized", new Date()));
        }
        String username = principal.getName();
        String originalUrl = request.getOriginalUrl().trim();
        if (originalUrl.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ShortenUrlError(false, "Original URL cannot be empty", new Date()));
        }

        if (request.getCustomName() != null && !request.getCustomName().trim().isEmpty()) {
            String customName = request.getCustomName().trim();
            if (customName.length() > 8) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ShortenUrlError(false, "Custom name must be less than 8 characters", new Date()));
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
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ShortenUrlError(false, "Expiration time cannot be in the past", new Date()));
                }
                Date maxExpirationDate100Years = new Date(currentDate.getTime() + (101L * 365 * 24 * 60 * 60 * 1000));
                if (expirationDate.after(maxExpirationDate100Years)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ShortenUrlError(false, "Expiration time cannot be greater than 101 years",
                                    new Date()));
                }
            } catch (ParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ShortenUrlError(false,
                        "Invalid date format. Use format: 'EEE MMM dd HH:mm:ss zzz yyyy' (e.g., 'Wed Mar 20 15:30:00 EDT 2024')",
                        new Date()));
            }
        }

        boolean isCustomNameTaken = shortUrlService.checkCustomName(request.getCustomName());
        if (isCustomNameTaken) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ShortenUrlError(false, "Custom name already taken", new Date()));
        }

        request.setOriginalUrl(originalUrl);
        request.setExpirationTime(expirationTime);

        ShortUrl shortUrl = shortUrlService.createShortUrl(request, username);

        if (shortUrl == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ShortenUrlError(false, "Custom name already taken", new Date()));
        }
        return ResponseEntity.ok(new ShortenUrlResponse(true, shortUrl.getId(), shortUrl.getOriginalUrl(),
                shortUrl.getShortUrl(), shortUrl.getUsername(), shortUrl.getCreatedAt(), shortUrl.getExpirationTime()));
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody ShortenUrlRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ShortenUrlError(false, "Unauthorized", new Date()));
        }
        String username = principal.getName();
        String originalUrl = request.getOriginalUrl().trim();
        if (originalUrl.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ShortenUrlError(false, "Original URL cannot be empty", new Date()));
        }

        if (request.getCustomName() != null && !request.getCustomName().trim().isEmpty()) {
            String customName = request.getCustomName().trim();
            if (customName.length() > 10) {
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
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ShortenUrlError(false, "Expiration time cannot be in the past", new Date()));
                }
                Date maxExpirationDate = new Date(currentDate.getTime() + (30L * 24 * 60 * 60 * 1000));
                if (expirationDate.after(maxExpirationDate)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ShortenUrlError(false,
                                    "Expiration time cannot be greater than 30 days for normal User", new Date()));
                }

            } catch (ParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ShortenUrlError(false,
                        "Invalid date format. Use format: 'EEE MMM dd HH:mm:ss zzz yyyy' (e.g., 'Wed Mar 20 15:30:00 EDT 2024')",
                        new Date()));
            }
        }

        request.setOriginalUrl(originalUrl);
        request.setCustomName(Helper.generateRandomString(request.getCustomName()));
        request.setExpirationTime(expirationTime);


        ShortUrl shortUrl = shortUrlService.createShortUrl(request, username);
        if (shortUrl == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ShortenUrlError(false, "Custom name already taken", new Date()));
        }
        return ResponseEntity.ok(new ShortenUrlResponse(true, shortUrl.getId(), shortUrl.getOriginalUrl(),
                shortUrl.getShortUrl(), shortUrl.getUsername(), shortUrl.getCreatedAt(), shortUrl.getExpirationTime()));
    }

    @DeleteMapping("/delete/{shortUrl}")
    public ResponseEntity<?> deleteShortUrl(@PathVariable String shortUrl, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ShortenUrlError(false, "Unauthorized", new Date()));
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

    @GetMapping("/shorten/{shortUrl}/analytics")
    public ResponseEntity<?> getAnalytics(@PathVariable String shortUrl) {
        Optional<ShortUrl> shortUrlObj = shortUrlRepository.findByShortUrl(shortUrl);
        if (shortUrlObj.isPresent()) {
            return ResponseEntity.ok(shortUrlObj.get().getAnalytics());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ShortenUrlError(false, "URL not found", new Date()));
    }
}
