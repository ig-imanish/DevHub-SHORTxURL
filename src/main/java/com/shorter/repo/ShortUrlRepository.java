package com.shorter.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shorter.models.ShortUrl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends MongoRepository<ShortUrl, String> {
    Optional<ShortUrl> findByShortUrl(String shortUrl);

    void deleteByShortUrl(String shortUrl);

    boolean existsByShortUrl(String shortUrl);

    void deleteByExpirationTimeBefore(Date date);

    List<ShortUrl> findByExpirationTimeBefore(Date date);

    List<ShortUrl> findByUsername(String username);
}
