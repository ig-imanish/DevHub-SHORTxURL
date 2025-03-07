package com.shorter.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "short_urls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrl {
    @Id
    private String id;
    private String originalUrl;
    private String shortUrl;
    private String username;
    private Date createdAt;
    private Date expirationTime;
    private boolean isSuspended = false;
    private boolean isSecuredWithPassword = false;
    private String password;

    private String referer;

    private int clickCount = 0; // Track total clicks
    private List<ClickAnalytics> analytics = new ArrayList<>();
}
