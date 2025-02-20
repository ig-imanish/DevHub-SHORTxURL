package com.shorter.service;

import org.springframework.stereotype.Service;

import com.shorter.model.ShortUrl;
import com.shorter.repo.ShortUrlRepository;

import java.util.Random;
import java.util.Date;
import java.util.Optional;
import java.text.SimpleDateFormat;
import java.text.ParseException;

@Service
public class ShortUrlService {
    private final ShortUrlRepository repository;

    public ShortUrlService(ShortUrlRepository repository) {
        this.repository = repository;
    }

    public ShortUrl createShortUrl(String originalUrl, String customName, String username, String expiryTime) {
        if (customName == null || customName.isEmpty()) {
            customName = generateRandomString();
        } else if (repository.existsByShortUrl(customName)) {
            return null; // Custom name already taken
        }

        Date currentTime = new Date();
        Date expirationTime;

        if (expiryTime == null || expiryTime.trim().isEmpty()) {
            expirationTime = new Date(currentTime.getTime() + (5 * 60 * 1000)); // Default 5 minutes
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                expirationTime = sdf.parse(expiryTime);
            } catch (ParseException e) {
                // If parsing fails, set default expiration time
                expirationTime = new Date(currentTime.getTime() + (5 * 60 * 1000));
            }
        }

        ShortUrl shortUrl = repository
                .save(new ShortUrl(null, originalUrl, customName, username, currentTime, expirationTime));

        return shortUrl;
    }

    // public ShortUrl createShortUrl(String originalUrl, String customName, String
    // username, String expiryTime) {
    // if (customName == null || customName.isEmpty()) {
    // customName = generateRandomString();
    // } else if (repository.existsByShortUrl(customName)) {
    // return null; // Custom name already taken
    // }

    // Date currentTime = new Date();
    // Date expirationTime;

    // if (expiryTime == null || expiryTime.trim().isEmpty()) {
    // expirationTime = new Date(currentTime.getTime() + (5 * 60 * 1000)); //
    // Default 5 minutes
    // } else {
    // try {
    // SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
    // expirationTime = sdf.parse(expiryTime);
    // } catch (ParseException e) {
    // // If parsing fails, set default expiration time
    // expirationTime = new Date(currentTime.getTime() + (5 * 60 * 1000));
    // }
    // }

    // ShortUrl shortUrl = repository
    // .save(new ShortUrl(null, originalUrl, customName, username, currentTime,
    // expirationTime));

    // return shortUrl;
    // }

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
    // return repository.findByShortUrl(shortUrl).map(ShortUrl::getOriginalUrl);
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
