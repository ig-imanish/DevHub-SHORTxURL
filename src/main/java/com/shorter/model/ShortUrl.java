package com.shorter.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Document(collection = "short_urls")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ShortUrl {
    @Id
    private String id;
    private String originalUrl;
    private String shortUrl;
    private String username;
    private Date createdAt;
    private Date expirationTime;
}

