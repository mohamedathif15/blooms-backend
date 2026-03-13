package com.blooms.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expiration;

    private Key key() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(
                        System.currentTimeMillis() + expiration))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, UserDetails user) {
        return extractUsername(token)
                .equals(user.getUsername())
                && !isExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private boolean isExpired(String token) {
        return extractClaim(token, Claims::getExpiration)
                .before(new Date());
    }

    public <T> T extractClaim(
            String token, Function<Claims, T> resolver) {
        return resolver.apply(
                Jwts.parserBuilder()
                        .setSigningKey(key()).build()
                        .parseClaimsJws(token).getBody());
    }
}