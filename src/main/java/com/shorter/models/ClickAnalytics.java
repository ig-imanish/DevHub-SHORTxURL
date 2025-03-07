package com.shorter.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClickAnalytics {
    private String ipAddress;
    private String userAgent;
    private String referrer;
    private String country; // For Geolocation tracking
    private LocalDateTime timestamp;

    // Constructor
    public ClickAnalytics(String ipAddress, String userAgent, String referrer, String country) {
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.referrer = referrer;
        this.country = country;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
}
