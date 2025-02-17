package com.shorter.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.shorter.repo.ShortUrlRepository;
import java.util.Date;

@Component
public class UrlCleanupScheduler {
    
    private final ShortUrlRepository repository;

    public UrlCleanupScheduler(ShortUrlRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 60000) // Runs every 1 minute
    public void cleanupExpiredUrls() {
        Date currentTime = new Date();
        repository.deleteByExpirationTimeBefore(currentTime);
        repository.findByExpirationTimeBefore(currentTime).forEach(System.out::println);
        System.out.println("Expired URLs cleaned up");
    }
}