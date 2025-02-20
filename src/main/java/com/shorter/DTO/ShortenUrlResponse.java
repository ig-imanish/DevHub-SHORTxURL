package com.shorter.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShortenUrlResponse {
    private boolean success;
    private String id;
    private String originalUrl;
    private String shortUrl;
    private String username;
    private Date createdAt;
    private Date expirationTime;
}
