package com.shorter.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtilities {

    // @Value("${jwt.secret}")
    // private String secret;

    // @Value("${jwt.expiration}")
    // private Long jwtExpiration;

    @Value("${auth.api.url}")
    private String API_BASE_URL;

    private String secret = "2b44b0b00fd822d8ce753e54dac3dc4e06c2725f7db930f3b9924468b53194dbccdbe23d7baa5ef5fbc414ca4b2e64700bad60c5a7c45eaba56880985582fba4";
    private Long jwtExpiration = 36000000L;

    private RestTemplate restTemplate;

    public JwtUtilities(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // public String extractUsername(String token) {
    // return extractClaim(token, Claims::getSubject);
    // }

    // public Claims extractAllClaims(String token) {
    // return Jwts.parserBuilder()
    // .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
    // .build()
    // .parseClaimsJws(token)
    // .getBody();
    // }

    // public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    // final Claims claims = extractAllClaims(token);
    // return claimsResolver.apply(claims);
    // }

    // public Date extractExpiration(String token) {
    // return extractClaim(token, Claims::getExpiration);
    // }

    // public Boolean validateToken(String token, UserDetails userDetails) {
    // final String email = extractUsername(token);
    // return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    // }

    // public Boolean isTokenExpired(String token) {
    // return extractExpiration(token).before(new Date());
    // }

    // public String generateToken(String email, List<String> roles) {

    // return Jwts.builder().setSubject(email).claim("role", roles).setIssuedAt(new
    // Date(System.currentTimeMillis()))
    // .setExpiration(Date.from(Instant.now().plus(jwtExpiration,
    // ChronoUnit.MILLIS)))
    // .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
    // .compact();
    // }

    // public boolean validateToken(String token) {
    // try {
    // Jwts.parserBuilder()
    // .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
    // .build()
    // .parseClaimsJws(token);
    // return true;
    // } catch (SecurityException e) {
    // log.info("Invalid JWT signature.");
    // log.trace("Invalid JWT signature trace: {}", e);
    // } catch (MalformedJwtException e) {
    // log.info("Invalid JWT token.");
    // log.trace("Invalid JWT token trace: {}", e);
    // } catch (ExpiredJwtException e) {
    // log.info("Expired JWT token.");
    // log.trace("Expired JWT token trace: {}", e);
    // } catch (UnsupportedJwtException e) {
    // log.info("Unsupported JWT token.");
    // log.trace("Unsupported JWT token trace: {}", e);
    // } catch (IllegalArgumentException e) {
    // log.info("JWT token compact of handler are invalid.");
    // log.trace("JWT token compact of handler are invalid trace: {}", e);
    // }
    // return false;
    // }

    public String getToken(HttpServletRequest httpServletRequest) {
        final String bearerToken = httpServletRequest.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        } // The part after "Bearer "
        return null;
    }

    /**
     * Validates a token and returns the associated email if valid, null otherwise
     */
    // public String validateTokenAndGetEmail(String token) {
    //     try {
    //         if (validateToken(token)) {
    //             return extractUsername(token);
    //         }
    //         return null;
    //     } catch (Exception e) {
    //         log.error("Error validating token: {}", e.getMessage());
    //         return null;
    //     }
    // }

    /**
     * Makes an API call to validate the token with an external auth service
     */
    /**
     * Makes an API call to validate the token with an external auth service
     */
    public String validateTokenWithApi(String token) {
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

            org.springframework.http.ResponseEntity<Map> response = restTemplate.exchange(
                    API_BASE_URL + "/auth/validateToken",
                    org.springframework.http.HttpMethod.POST,
                    entity,
                    Map.class);

            if (response.getStatusCode() == org.springframework.http.HttpStatus.OK) {
                if (response.getBody() != null && response.getBody().containsKey("email")) {
                    return (String) response.getBody().get("email");
                }
                return null;
            }
            return null;
        } catch (Exception e) {
            log.error("Error validating token with API: {}", e.getMessage());
            return null;
        }
    }

}
