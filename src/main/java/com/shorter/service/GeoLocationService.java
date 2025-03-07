package com.shorter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoLocationService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getCountryFromIP(String ipAddress) {
        try {
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("::1")) {
                return "Localhost"; // Skip geolocation for local requests
            }
            String url = "http://ip-api.com/json/" + ipAddress;
            GeoLocationResponse response = restTemplate.getForObject(url, GeoLocationResponse.class);
            return (response != null) ? response.getCountry() : "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }

    static public class GeoLocationResponse {
        private String country;

        public String getCountry() {
            return country;
        }
    }

}
