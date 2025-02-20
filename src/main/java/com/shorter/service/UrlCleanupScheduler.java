package com.shorter.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shorter.model.ShortUrl;
import com.shorter.repo.ShortUrlRepository;
import java.util.Date;
import java.util.List;

@Component
public class UrlCleanupScheduler {

    private final ShortUrlRepository repository;

    public UrlCleanupScheduler(ShortUrlRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 30000) // Runs every 30 seconds
    public void cleanupExpiredUrls() {
        Date currentTime = new Date();
        repository.deleteByExpirationTimeBefore(currentTime);
        List<ShortUrl> expiredUrls = repository.findByExpirationTimeBefore(currentTime);
        int count = expiredUrls.size();
        expiredUrls.forEach(url -> System.out.println(url));
        System.out.println("Expired URLs cleaned up " + count);
    }
}