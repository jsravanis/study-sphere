package com.studysphere.backend.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    // Generate a secure key for HS256
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(System.getenv("JWT_SECRET_KEY")));
    private static final long expirationTime = 1000 * 60 * 60 * 24;  // Token expires in 24 hours


    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
