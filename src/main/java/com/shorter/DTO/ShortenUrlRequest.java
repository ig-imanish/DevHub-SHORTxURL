package com.shorter.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShortenUrlRequest {
    private String originalUrl;
    private String customName;
    private String expirationTime;
}
