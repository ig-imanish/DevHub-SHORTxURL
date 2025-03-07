package com.shorter.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shorter.models.ClickAnalytics;
import com.shorter.models.ShortUrl;
import com.shorter.repo.ShortUrlRepository;
import com.shorter.service.GeoLocationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class redirectUrlController {

    private final ShortUrlRepository shortUrlRepository;
    private final GeoLocationService geoLocationService;

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> redirectToOriginalUrl(@PathVariable String shortUrl, HttpServletRequest request) {
        Optional<ShortUrl> shortUrlOptional = shortUrlRepository.findByShortUrl(shortUrl);

        if (shortUrlOptional.isPresent()) {
            ShortUrl url = shortUrlOptional.get();

            // Get real client IP (handling proxies like Ngrok)
            String ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = request.getRemoteAddr(); // Fallback to default if header is missing
            }

            String userAgent = request.getHeader("User-Agent");
            String referrer = request.getHeader("Referer");
            String country = geoLocationService.getCountryFromIP(ipAddress); // Get country

            // Save analytics data
            ClickAnalytics analytics = new ClickAnalytics(ipAddress, userAgent, referrer, country);
            url.getAnalytics().add(analytics);
            url.setClickCount(url.getClickCount() + 1);
            shortUrlRepository.save(url);

            // Redirect to original URL
            return ResponseEntity.status(302).location(URI.create(url.getOriginalUrl())).build();
        } else {
            return ResponseEntity.status(302).location(URI.create("https://www.google.com")).build();
        }
    }

}
