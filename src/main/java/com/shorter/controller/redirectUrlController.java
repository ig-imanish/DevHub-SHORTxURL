package com.shorter.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shorter.service.ShortUrlService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class redirectUrlController {

    private final ShortUrlService shortUrlService;

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> redirectToOriginal(@PathVariable String shortUrl) {
        Optional<String> originalUrl = shortUrlService.getOriginalUrl(shortUrl);
        return originalUrl.map(url -> ResponseEntity.status(302).location(URI.create(url)).build())
                .orElse(ResponseEntity.notFound().build());
    }
}
