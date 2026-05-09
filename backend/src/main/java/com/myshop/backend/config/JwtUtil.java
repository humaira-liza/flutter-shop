package com.myshop.backend.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "mySuperSecretKeyThatIsAtLeast32CharactersLong!";

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // 🔥 PARSE TOKEN
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 🔥 GET EMAIL FROM TOKEN
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // 🔥 GENERATE TOKEN (EMAIL MUST BE SUBJECT)
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // 🔥 VERY IMPORTANT
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}