package com.taskflow.security;

import io.jsonwebtoken.*;
// import io.jsonwebtoken.io.Decoders;

import javax.crypto.SecretKey; // Keep this import
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    // FIX: Changed from 'Key' to 'SecretKey'
    private final SecretKey key; 
    private final long exp;

    public JwtService(@Value("${app.jwt.secret}") String s, @Value("${app.jwt.expiration-ms}") long e) {
        // Keys.hmacShaKeyFor returns a SecretKey, which perfectly matches now
        key = Keys.hmacShaKeyFor(s.getBytes(StandardCharsets.UTF_8));
        exp = e;
    }

    public String generate(Long id, String email) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", id)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + exp))
                .signWith(key) // Works perfectly with SecretKey
                .compact();
    }

    public String email(String t) {
        // FIX: .verifyWith(key) will now accept it without errors
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(t)
                .getPayload()
                .getSubject();
    }

    public boolean valid(String t) {
        try {
            email(t);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}