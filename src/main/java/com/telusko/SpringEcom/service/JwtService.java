package com.telusko.SpringEcom.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private Key getKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 7))  // 3 min
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //last method

    //You can retrieve username here to load user from db in filter only, checkroles.
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);   // will throw if invalid/expired/tampered
            System.out.println("This ran successfully");
            return true; // token is valid
        } catch (Exception e) {
            System.out.println("cought an error"+e.getMessage());
            return false; // invalid token
        }
    }

}
