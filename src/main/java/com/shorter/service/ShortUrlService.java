package com.shorter.service;

import org.springframework.stereotype.Service;

import com.shorter.model.ShortUrl;
import com.shorter.repo.ShortUrlRepository;

import java.util.Random;
import java.util.Date;
import java.util.Optional;

@Service
public class ShortUrlService {
    private final ShortUrlRepository repository;

    public ShortUrlService(ShortUrlRepository repository) {
        this.repository = repository;
    }

    public String createShortUrl(String originalUrl, String customName, String username) {
        if (customName == null || customName.isEmpty()) {
            customName = generateRandomString();
        } else if (repository.existsByShortUrl(customName)) {
            return null; // Custom name already taken
        }
        
        Date currentTime = new Date();
        Date expirationTime = new Date(currentTime.getTime() + (1 * 60 * 1000)); // Current time + 5 minutes
        
        repository.save(new ShortUrl(null, originalUrl, customName, username, currentTime, expirationTime));
        return customName;
    }

    public Optional<String> getOriginalUrl(String shortUrl) {
        Date currentTime = new Date();
        Optional<ShortUrl> url = repository.findByShortUrl(shortUrl);
        
        if (url.isPresent() && url.get().getExpirationTime().before(currentTime)) {
            repository.deleteByShortUrl(shortUrl);
            return Optional.empty();
        }
        
        return url.map(ShortUrl::getOriginalUrl);
    }
    // public Optional<String> getOriginalUrl(String shortUrl) {
    //     return repository.findByShortUrl(shortUrl).map(ShortUrl::getOriginalUrl);
    // }

    private String generateRandomString() {
        int length = 5;
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    public boolean deleteShortUrl(String shortUrl) {
        if (repository.existsByShortUrl(shortUrl)) {
            repository.deleteByShortUrl(shortUrl);
            return true;
        }
        return false;
    }
}

